package com.recipes.aop;


import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.recipes.dao.RecipeDAO;
import com.recipes.entity.Recipe;
import com.recipes.utils.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.recipes.utils.RedisConstants.*;

@Slf4j
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    private  RecipeDAO recipeDAO;

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    public CacheClient(StringRedisTemplate stringRedisTemplate, RecipeDAO recipeDAO) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.recipeDAO = recipeDAO;
    }

    public void set (String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value),time, unit);
    }

    public void saveRecipeRedis(Long id, Long expireSeconds) {
        // 1. 从数据库查询数据
        Recipe recipe = recipeDAO.findRecipeById(id);
        if (recipe == null) {
            // 如果数据不存在，将空值写入Redis防止缓存穿透
            stringRedisTemplate.opsForValue().set(CACHE_RECIPE_KEY + id, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            return;
        }
        // 2. 写入Redis并设置逻辑过期时间
        this.setWithLogicalExpire(CACHE_RECIPE_KEY + id, recipe, expireSeconds, TimeUnit.SECONDS);
    }


    public void setWithLogicalExpire (String key, Object value, Long time, TimeUnit unit) {
        //set logical out of time
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        //write into redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    public Recipe queryWithPassThrough(String keyPrefix,Long id) {
        String key = keyPrefix + id;
        //1.use redis to get recipe cache
        String recipeJson = stringRedisTemplate.opsForValue().get(key);
        //2.determine whether it exists
        if (StrUtil.isNotBlank(recipeJson)) {
            //3. return recipe
            return JSONUtil.toBean(recipeJson, Recipe.class);
        }
        //determine whether the hit is a null value
        if (recipeJson != null) {
            return null;
        }
        //4.not exist, query the database by id
        Recipe recipe = recipeDAO.findRecipeById(id);

        //5.not exist, return false
        if(recipe == null){
            //write null value
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        //6. exist, write into redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(recipe), CACHE_RECIPE_TTL, TimeUnit.MINUTES);
        return recipe;
    }

    public Recipe queryWithLogicalExpire(String keyPrefix,Long id) {
        String key = keyPrefix + id;
        String recipeJson = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(recipeJson)) {
            return null;
        }
        RedisData redisData = JSONUtil.toBean(recipeJson, RedisData.class);
        Recipe recipe = JSONUtil.toBean((JSONObject) redisData.getData(), Recipe.class);
        LocalDateTime expireTime = redisData.getExpireTime();

        if (expireTime == null || expireTime.isAfter(LocalDateTime.now())) {
            return recipe;
        }

        String lockKey = LOCK_RECIPE_KEY + id;
        boolean isLock = tryLock(lockKey);
        if (isLock) {

            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    this.saveRecipeRedis(id, 20L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unlock(lockKey);
                }

            });
        }
        return recipe;

    }

    //互斥锁解决缓存击穿（高并发
    public Recipe queryWithMutex(Long id) {
        String key = CACHE_RECIPE_KEY + id;
        String recipeJson = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(recipeJson)) {
            Recipe recipe = JSONUtil.toBean(recipeJson, Recipe.class);
            return recipe;
        }
        if (recipeJson == null) {
            return null;
        }
        String lockKey = "lock:recipe" + id;
        Recipe recipe = null;
        try {
            boolean isLock = tryLock(lockKey);
            if (!isLock) {
                Thread.sleep(50);
                return queryWithMutex(id);
            }
            recipe = recipeDAO.findRecipeById(id);
            Thread.sleep(200);
            if (recipe == null) {
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(recipe), CACHE_RECIPE_TTL, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock(lockKey);
        }

        return recipe;
    }


}
