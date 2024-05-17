package com.m7.abs.common.constant.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 导出任务
 * 1、待执行；2、导出中；3、已暂停；4、导出完成；5、导出失败；
 *
 * @author Kejie Peng
 * @date 2023年 03月27日 10:39:27
 */
@Getter
public enum ExportStatusEnum {

    INITIAL(1, "待执行"),
    EXECUTION(2, "导出中"),
    PAUSE(3, "已暂停"),
    COMPETE(4, "导出完成"),
    FAIL(5, "导出失败");

    ExportStatusEnum(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    /**
     * 标记数据库存的值是code
     */
    @EnumValue
    private final int code;
    private final String descp;
}
