package com.m7.abs.common.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Joiner;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-08-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("enterprise_conf")
public class EnterpriseConfEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    @TableId("enterprise_id")
    private String enterpriseId;

    /**
     * 是否允许oss地址只推送相对路径地址;默认:否
     */
    private Boolean ossPushFilePathOnlyEnable = false;

    /**
     * oss文件请求地址代理;默认:否
     */
    private Boolean ossProxyEnable = false;

    /**
     * 对象存储配置,多个配置间用 ; 号隔开
     */
    private String storageConf;

    /**
     * 逻辑删除:
     * 0:未删除;1:已删除;
     */
    @TableLogic
    private int deleted;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(value = "last_update_by", fill = FieldFill.UPDATE)
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_update_time", fill = FieldFill.UPDATE)
    private Date lastUpdateTime;

    @TableField(exist = false)
    private List<String> storageConfList;

    public void setStorageConfList(List<String> storageConf) {
        if (storageConf != null) {
            this.storageConf = Joiner.on(";").join(storageConf);
        } else {
            this.storageConf = "";
        }
    }

    public List<String> getStorageConfList() {
        if (StringUtils.isNotEmpty(this.storageConf)) {
            return Arrays.asList(this.storageConf.split(";"));
        }
        return null;
    }
}
