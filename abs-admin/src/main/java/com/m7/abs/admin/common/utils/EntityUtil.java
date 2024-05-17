package com.m7.abs.admin.common.utils;

import com.m7.abs.admin.core.security.SecurityUtil;
import com.m7.abs.common.domain.base.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/23 10:44
 */
public class EntityUtil {
    public static void initEditor(BaseEntity baseEntity) {
        if (baseEntity == null) {
            return;
        }
        if (StringUtils.isEmpty(baseEntity.getId())) {
            baseEntity.setCreateBy(SecurityUtil.getUsername());
            baseEntity.setCreateTime(new Date());
        } else {
            baseEntity.setLastUpdateBy(SecurityUtil.getUsername());
            baseEntity.setLastUpdateTime(new Date());
        }
    }
}
