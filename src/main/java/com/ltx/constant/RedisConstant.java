package com.ltx.constant;

public interface RedisConstant {
    // 验证码
    String LOGIN_CODE_KEY = "login:code:";
    Long LOGIN_CODE_TTL = 2L;
    // token
    String LOGIN_TOKEN_KEY = "login:token:";
    Long LOGIN_TOKEN_TTL = 30L;
    // cache
    String CACHE_SHOP_KEY = "cache:shop:";
    Long CACHE_SHOP_TTL = 30L;
    String CACHE_SHOP_TYPE_KEY = "cache:shopType:";
    Long CACHE_SHOP_TYPE_TTL = 30L;
    Long CACHE_NULL_TTL = 2L;
    // lock
    String LOCK_SHOP_KEY = "lock:shop:";
    Long LOCK_SHOP_TTL = 10L;

    String SECKILL_STOCK_KEY = "seckill:stock:";
    String BLOG_LIKED_KEY = "blog:liked:";
    String FEED_KEY = "feed:";
    String SHOP_GEO_KEY = "shop:geo:";
    String USER_SIGN_KEY = "sign:";
}
