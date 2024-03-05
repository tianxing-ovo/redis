package com.ltx.mq;

import com.ltx.constant.RedisConstant;
import io.github.tianxingovo.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消费者
 */
@Component
public class Consumer {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ThreadPoolExecutor executor;

    @SuppressWarnings("InfiniteLoopStatement")
    @PostConstruct
    public void init() {
        executor.execute(() -> {
            while (true) {
                String message = consumeMessage(RedisConstant.MessageQueue.QUEUE_NAME);
                if (Objects.isNull(message)) {
                    continue;
                }
                handleMessage(message);
            }
        });
    }

    /**
     * 消费消息
     *
     * @param queueName 队列名称
     * @return 消息内容
     */
    public String consumeMessage(String queueName) {
        return redisUtil.BlockingRightPop(queueName, 5, TimeUnit.SECONDS);
    }

    /**
     * 处理消息
     */
    public void handleMessage(String message) {
        System.out.println(message);
    }
}
