package com.recipes.utils;

public class RedisConstants {

    public final static String CACHE_RECIPE_KEY = "cache:recipe";
    public final static Long CACHE_RECIPE_TTL = 30L;
    public final static Long CACHE_NULL_TTL = 2L;


    public final static String LOCK_RECIPE_KEY = "lock:recipe";
    public final static Long LOCK_RECIPE_TTL = 10L;
}
