package com.m7.abs.api.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m7.abs.common.exception.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 和 api 接口 json 序列化/反序列化 配置保持一致
 *
 * @author zhuhf
 */
@Component
public class JsonComponent {
    private static final Logger logger = LoggerFactory.getLogger(JsonComponent.class);
    private final ObjectMapper objectMapper;

    public JsonComponent(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] writeValueAsBytes(Object value) {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            logger.error("json 序列化异常, object: {}", value, e);
            throw new JsonException(e);
        }
    }

    public String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            logger.error("json 序列化异常, object: {}", value, e);
            throw new JsonException(e);
        }
    }

    public <T> T readValue(byte[] src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (IOException e) {
            logger.error("json 反序列化异常, bytes: {}, classType: {}", src, valueType, e);
            throw new JsonException(e);
        }
    }
}
