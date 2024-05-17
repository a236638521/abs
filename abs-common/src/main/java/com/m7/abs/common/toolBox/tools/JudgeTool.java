package com.m7.abs.common.toolBox.tools;

import com.m7.abs.common.annotation.Spanner;
import com.m7.abs.common.annotation.ToolBoxType;
import com.m7.abs.common.exception.SpannerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 逻辑判断工具类
 */
@Slf4j
@ToolBoxType(name = "JUDGE")
public class JudgeTool {
    /**
     * 包含
     *
     * @param v1
     * @param v2
     * @return0
     */
    @Spanner(name = "CONTAIN")
    public static boolean contain(String v1, String v2) {
        try {
            if (StringUtils.isNotEmpty(v1) && StringUtils.isNotEmpty(v2)) {
                return v1.contains(v2);
            }
            return false;
        } catch (Exception e) {
            String msg = "Tool [JUDGE] spanner [CONTAIN] invoke fail";
            log.error(msg, e);
            throw new SpannerException(msg);
        }
    }

    /**
     * 不包含
     *
     * @param v1
     * @param v2
     * @return
     */
    @Spanner(name = "NOT_CONTAIN")
    public static boolean notContain(String v1, String v2) {
        try {
            return !contain(v1, v2);
        } catch (Exception e) {
            String msg = "Tool [JUDGE] spanner [NOT_CONTAIN] invoke fail";
            log.error(msg, e);
            throw new SpannerException(msg);
        }
    }

    /**
     * 等于
     *
     * @param v1
     * @param v2
     * @return
     */
    @Spanner(name = "EQUALS")
    public static boolean equals(Object v1, Object v2) {
        try {
            return v1.equals(v2);
        } catch (Exception e) {
            String msg = "Tool [JUDGE] spanner [EQUALS] invoke fail";
            log.error(msg, e);
            throw new SpannerException(msg);
        }

    }

    /**
     * 不等于
     * @param v1
     * @param v2
     * @return
     */
    @Spanner(name = "NOT_EQUALS")
    public static boolean notEquals(Object v1, Object v2) {
        try {
            return !v1.equals(v2);
        } catch (Exception e) {
            String msg = "Tool [JUDGE] spanner [NOT_EQUALS] invoke fail";
            log.error(msg, e);
            throw new SpannerException(msg);
        }

    }
}
