package com.m7.abs.common.toolBox.tools;

import com.m7.abs.common.annotation.Spanner;
import com.m7.abs.common.annotation.ToolBoxType;
import com.m7.abs.common.exception.SpannerException;
import com.m7.abs.common.utils.ConverterUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据类型转换工具类
 */
@Slf4j
@ToolBoxType(name = "TYPE_CONVERT")
public class TypeConvertTool {

    /**
     * 数据类型强制转换
     * @param type
     * @param v1
     * @return
     */
    @Spanner(name = "CONVERT")
    public static Object convert(String type, Object v1) {
        try {
            switch (type) {
                case "STRING":
                    return ConverterUtil.toString(v1);
                case "INTEGER":
                    return ConverterUtil.toInteger(v1);
                case "DOUBLE":
                    return ConverterUtil.toDouble(v1);
                case "LONG":
                    return ConverterUtil.toLong(v1);
                case "FLOAT":
                    return ConverterUtil.toFloat(v1);
                default:
                    return null;
            }
        } catch (Exception e) {
            String msg = "Tool [TYPE_CONVERT] spanner [CONVERT] invoke fail";
            log.error(msg, e);
            throw new SpannerException(msg);
        }
    }
}
