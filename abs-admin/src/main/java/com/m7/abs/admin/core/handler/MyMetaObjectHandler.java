package com.m7.abs.admin.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.m7.abs.admin.core.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis_plus
 * 自动修改createBy createTime lastUpdateBy lastUpdateTime
 *
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/16 11:27
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // 获取到需要被填充的字段值
        Object createBy = getFieldValByName("createBy", metaObject);
        Object createTime = getFieldValByName("createTime", metaObject);
        if (createBy == null) {
            this.strictInsertFill(metaObject, "createBy", String.class, SecurityUtil.getUsername());
        }
        if (createTime == null) {
            this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "lastUpdateBy", String.class, SecurityUtil.getUsername());
        this.strictUpdateFill(metaObject, "lastUpdateTime", Date.class, new Date());
    }
}
