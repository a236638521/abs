package com.m7.abs.admin.domain.bean;

import com.m7.abs.common.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/5/15 14:29
 */
@Getter
@Setter
public class ConditionBean {
    /**
     * 数据库字段名
     */
    private String column;
    /**
     * 字段值
     */
    private Object value;
    /**
     * 连接类型，如eq,ne,gt,ge,lt,le,contains,like,leftLike,rightLike,notLike
     */
    private String type;
    /**
     * 对应的表达式
     */
    private String operator;
    /**
     * 数据库类型: 默认为String
     * 如:
     * string,long,int,date
     */
    private String objectType = "string";


    public Object getValue() {
        if (StringUtils.isNotEmpty(objectType) && value != null) {
            String s = String.valueOf(value);
            switch (objectType) {
                case "long":
                    value = StringUtils.isNotEmpty(s) ? Long.valueOf(s) : null;
                    break;
                case "int":
                    value = StringUtils.isNotEmpty(s) ? Integer.valueOf(s) : null;
                    break;
                case "date":
                    if (StringUtils.isNotEmpty(type) && "between".equals(type)) {
                        if (value instanceof List) {
                            value = (List) value;
                            List<Date> list = new ArrayList<>();
                            if (((List<?>) value).size() == 2) {
                                for (int i = 0; i < ((List<?>) value).size(); i++) {
                                    Object o = ((List<?>) value).get(i);
                                    list.add(DateUtil.parseStrToDate((String) o, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
                                }
                            }
                            return list;
                        }
                    } else {
                        return DateUtil.parseStrToDate((String) value, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
                    }
                    break;
                default:
                    break;
            }
        }
        return value;
    }

    public String getOperator() {
        if (StringUtils.isEmpty(this.operator) && StringUtils.isNotEmpty(this.type)) {
            switch (this.type) {
                case "contains":
                    this.operator = "in";
                    break;
                case "eq":
                    this.operator = "=";
                    break;
                case "ne":
                    this.operator = "!=";
                    break;
                case "like":
                    this.operator = "LIKE";
                    break;
                case "leftLike":
                    this.operator = "";
                    break;
                case "rightLike":
                    this.operator = "";
                    break;
                case "notLike":
                    this.operator = "";
                    break;
                case "gt":
                    this.operator = ">";
                    break;
                case "lt":
                    this.operator = "<";
                    break;
                case "ge":
                    this.operator = ">=";
                    break;
                case "le":
                    this.operator = "<=";
                    break;
            }
        }

        return this.operator;
    }
}
