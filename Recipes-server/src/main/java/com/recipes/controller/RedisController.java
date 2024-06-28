package com.recipes.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Redis API")
public class RedisController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/set")
    public String set() {
        redisTemplate.opsForValue().set("key", "value");
        return "Set key=value";
    }

    @GetMapping("/get")
    public String get() {
        return "Value for key: " + redisTemplate.opsForValue().get("key");
    }
}
