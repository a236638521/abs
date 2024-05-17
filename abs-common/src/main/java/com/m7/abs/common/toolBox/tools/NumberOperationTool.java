package com.m7.abs.common.toolBox.tools;

import com.m7.abs.common.annotation.Spanner;
import com.m7.abs.common.annotation.ToolBoxType;
import com.m7.abs.common.utils.MathUtil;
import com.m7.abs.common.utils.MyStringUtils;

/**
 * 数字运算工具类
 */
@ToolBoxType(name = "NUMBER_OPERATION")
public class NumberOperationTool {

    /**
     * 加法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    @Spanner(name = "ADD")
    public static String add(String v1, String... v2) throws Exception {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(v1)) {
            Double o1 = (Double) MyStringUtils.transformValue(double.class, v1);
            if (v2 != null) {
                for (int i = 0; i < v2.length; i++) {
                    Double o2 = (Double) MyStringUtils.transformValue(double.class, v2[i]);
                    o1 = MathUtil.add(o1, o2);
                }
            }

            return o1.toString();
        }

        return null;
    }


    /**
     * 减法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    @Spanner(name = "SUB")
    public static double sub(double v1, double... v2) {
        if (v2 != null) {
            for (int i = 0; i < v2.length; i++) {
                double v = v2[i];
                v1 = MathUtil.sub(v1, v);
            }
        }
        return v1;
    }

    /**
     * 乘法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    @Spanner(name = "MUL")
    public static double mul(double v1, double... v2) {
        if (v2 != null) {
            for (int i = 0; i < v2.length; i++) {
                double v = v2[i];
                v1 = MathUtil.mul(v1, v);
            }
        }
        return v1;
    }

    /**
     * 除法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    @Spanner(name = "DIV")
    public static double div(double v1, double... v2) {
        if (v2 != null) {
            for (int i = 0; i < v2.length; i++) {
                double v = v2[i];
                v1 = MathUtil.div(v1, v);
            }
        }
        return v1;
    }
}
