package com.jameswu.demo.model;

import lombok.Data;

@Data
// @AllArgsConstructor
public abstract class RedisMessage {
    boolean isAdd;

    public RedisMessage(boolean isAdd) {
        this.isAdd = isAdd;
    }
}
