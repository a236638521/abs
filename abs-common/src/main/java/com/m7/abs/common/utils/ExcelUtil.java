package com.m7.abs.common.utils;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.reflect.Field;

public class ExcelUtil {
    /**
     * 设置excel样式
     * 返回样式 Style 具体细节可以在网上查找样式元素 自行替换 现在这个也是可以直接套用的
     *
     * @return
     */
    public static HorizontalCellStyleStrategy getStyleStrategy() {
        // 头的策略  样式调整
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 头背景 浅绿
        headWriteCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        WriteFont headWriteFont = new WriteFont();
        // 头字号
        headWriteFont.setFontHeightInPoints((short) 12);
        // 字体样式
        headWriteFont.setFontName("宋体");
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 自动换行
        headWriteCellStyle.setWrapped(false);
        // 设置细边框
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        // 设置边框颜色 25灰度
        headWriteCellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headWriteCellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headWriteCellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headWriteCellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // 水平对齐方式
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 垂直对齐方式
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 内容的策略 宋体

        /**
         *
         */
        WriteCellStyle contentStyle = new WriteCellStyle();
        // 设置垂直居中
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置 水平居中
        contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        WriteFont contentWriteFont = new WriteFont();
        // 内容字号
        contentWriteFont.setFontHeightInPoints((short) 12);
        // 字体样式
        contentWriteFont.setFontName("宋体");
        contentStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentStyle);
    }

    // 下面这两个方法没用到过 之前也是粘贴过来的现在不知道 又什么用 (>@_@<)!!
    public static Boolean isEmpty(String className, Object excel) {
        try {
            Class clazz = Class.forName(className);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 允许通过反射访问该字段
            }
            for (Field field : fields) {
                if (field.get(excel) != null) {
                    field.setAccessible(false);
                    return Boolean.FALSE;
                }
                field.setAccessible(false);
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }


}
