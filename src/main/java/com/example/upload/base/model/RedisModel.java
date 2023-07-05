package com.example.upload.base.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RedisModel {
    private String key;
    private String value;
}
