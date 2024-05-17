package com.m7.abs.common.toolBox.handler;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.Map;

@Getter
@Setter
public class ToolHandler {
    /**
     * 类对象
     */
    private Class clazz;
    /**
     * 方法对象
     */
    private Map<String, Method> methods;
}
