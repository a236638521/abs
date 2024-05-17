package com.m7.abs.common.toolBox.processor;

import com.m7.abs.common.annotation.Spanner;
import com.m7.abs.common.annotation.ToolBoxType;
import com.m7.abs.common.toolBox.handler.ToolHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SpannerProcessor {
    private static String PACKAGE = "com.m7.abs.common.toolBox.tools";

    //存放映射
    public static HashMap<String, ToolHandler> spannerMap = null;

    private static SpannerProcessor instance;

    private SpannerProcessor() {
        spannerMap = (HashMap<String, ToolHandler>) scanTools();
    }

    public static SpannerProcessor getInstance() {
        if (instance == null) {
            synchronized (SpannerProcessor.class) {
                if (instance == null) {
                    instance = new SpannerProcessor();
                }
            }
        }
        return instance;
    }


    /**
     * 扫描解析指定包下的@Controller和@RequestMapping注解
     * <p>
     * 只能扫描class文件，暂未实现扫描jar包
     *
     * @return 生成的映射表
     */
    public static Map<String, ToolHandler> scanTools() {
        Map<String, ToolHandler> tools = new HashMap<>();

        String[] packages = {PACKAGE};
        Set<Class<?>> classSet = null;
        try {
            classSet = new LoadPackageClazz(packages, ToolBoxType.class).getClassSet();
        } catch (Exception e) {
            log.error("get ToolBox class error", e);
        }
        if (classSet != null) {
            classSet.stream().forEach(clazz -> {
                ToolBoxType toolBoxType = clazz.getAnnotation(ToolBoxType.class);
                if (toolBoxType != null) {
                    String toolBoxName = toolBoxType.name();
                    ToolHandler toolHandler = new ToolHandler();
                    Map<String, Method> methods = new HashMap<>();

                    toolHandler.setClazz(clazz);

                    for (Method method : clazz.getDeclaredMethods()) {
                        Spanner spanner = method.getAnnotation(Spanner.class);
                        if (spanner != null) {
                            methods.put(spanner.name(), method);
                        }
                    }
                    toolHandler.setMethods(methods);

                    tools.put(toolBoxName, toolHandler);
                }

            });
        }

        return tools;
    }


}
