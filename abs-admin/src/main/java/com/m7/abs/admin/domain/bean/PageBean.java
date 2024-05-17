package com.m7.abs.admin.domain.bean;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.m7.abs.common.utils.MyStringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页bean
 */
@Getter
@Setter
public class PageBean extends Page {
    /**
     * 最大分页限制10000条
     */
    @Getter
    @Setter
    protected Long maxLimit = 10000L;


    /**
     * 过滤参数
     */
    private List<ConditionBean> conditionList = new ArrayList<>();
    public List<ConditionBean> sqlConditionList = new ArrayList<>();

    @JsonIgnore
    public List<ConditionBean> getSqlConditionList() {
        for (ConditionBean conditionVo : this.conditionList) {
            String operator = conditionVo.getOperator();
            if (operator != null && conditionVo.getValue() != null) {
                sqlConditionList.add(conditionVo);
            }
        }

        return sqlConditionList;
    }

    @JsonIgnore
    public QueryWrapper getQueryWrapper() {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (this.conditionList != null) {
            for (ConditionBean conditionVo : this.conditionList) {
                String column = conditionVo.getColumn();
                Object value = conditionVo.getValue();
                String objectType = conditionVo.getObjectType();
                if (value != null && !value.equals("")) {

                    column = translateColumn(column);
                    switch (conditionVo.getType()) {
                        case "contains":
                            if (value instanceof ArrayList) {
                                queryWrapper.in(column, ((ArrayList<?>) value).toArray());
                            }
                            break;
                        case "eq":
                            queryWrapper.eq(column, value);
                            break;
                        case "ne":
                            queryWrapper.ne(column, value);
                            break;
                        case "like":
                            queryWrapper.like(column, value);
                            break;
                        case "leftLike":
                            queryWrapper.likeLeft(column, value);
                            break;
                        case "rightLike":
                            queryWrapper.likeRight(column, value);
                            break;
                        case "notLike":
                            queryWrapper.notLike(column, value);
                            break;
                        case "gt":
                            queryWrapper.gt(column, value);
                            break;
                        case "lt":
                            queryWrapper.lt(column, value);
                            break;
                        case "ge":
                            queryWrapper.ge(column, value);
                            break;
                        case "le":
                            queryWrapper.le(column, value);
                            break;
                        case "groupBy":
                            queryWrapper.groupBy(column);
                            break;
                        case "orderByD":
                            queryWrapper.orderByDesc(column);
                            break;
                        case "orderByA":
                            queryWrapper.orderByAsc(column);
                            break;
                        case "isNull":
                            queryWrapper.isNull(column);
                            break;
                        case "isNotNull":
                            queryWrapper.isNotNull(column);
                            break;
                        case "between":
                            if (value instanceof List) {
                                value = (List) value;
                                if (((List<?>) value).size() == 2) {
                                    queryWrapper.between(column, ((List<?>) value).get(0), ((List<?>) value).get(1));
                                }
                            }

                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 翻译字段，将驼峰类型字段转译成下划线字段
     *
     * @param column
     * @return
     */
    private String translateColumn(String column) {
        if (StringUtils.isEmpty(column)) {
            return column;
        }
        String[] split = column.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            item = MyStringUtils.camelToUnderline(item);
            stringBuilder.append(item);
            if (i != split.length - 1) {
                stringBuilder.append(".");
            }
        }
        return stringBuilder.toString();
    }
}