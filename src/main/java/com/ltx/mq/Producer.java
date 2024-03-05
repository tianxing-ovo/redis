package com.ltx.mq;

import io.github.tianxingovo.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 生产者
 */
@Component
public class Producer {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 发送消息
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */
    public void sendMessage(String queueName, String message) {
        redisUtil.leftPush(queueName, message);
    }
}
