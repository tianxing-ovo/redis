package com.ltx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisData {
    // 逻辑过期时间
    private LocalDateTime expireTime;
    // 存入redis的数据
    private Object data;
}
