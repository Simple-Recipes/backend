package com.recipes.constant;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final long LOGIN_CODE_TTL = 30; // 30 minutes
    public static final long LOGIN_USER_TTL = 86400000L; // 1 day
    public static final String LOGIN_USER_KEY = "login:user:";
    public static final String LOGIN_USER_TOKEN_KEY = "login:user:token:";


}
