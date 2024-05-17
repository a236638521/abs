package com.m7.abs.api.domain.vo.midNum;

import com.m7.abs.common.constant.common.MidNumType;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 * AXB绑定请求参数
 *
 * @author hulin
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BindGXBRequestVO {
    /**
     * 小号,中间号;使用指定中间号进行号码绑定，如某些通道不支持指定中间号，则需要确定channelCode的值。
     */
    //@NotEmpty
    @Size(max = 20)
    private String telX;
    /**
     * 账号
     */
    @NotEmpty
    @Size(max = 40)
    private String account;
    /**
     * 号码集合ID
     */
    @NotEmpty
    @Size(max = 20)
    private String groupId;
    /**
     * 被叫号码
     */
    @NotEmpty
    @Size(max = 20)
    private String telB;

    /**
     * 是否需要录音 true:是;false:否,默认否
     */
    @Builder.Default
    private boolean needRecord = false;

    /**
     * 话单接收地址
     */
    @Size(max = 250)
    private String recordReceiveUrl;

    /**
     * 自定义字段 会在账单和话单推送时回传回来
     */
    @Size(max = 250)
    private String userData;
    /**
     * 绑定时长, 单位:秒。
     */
    @Builder.Default
    private Long expiration = 0L;
    /**
     * 地区区号
     */
    private Integer areaCode;

    /**
     * 通道编码,只有当telX为空的时候可以指定使用哪个通道
     */
    private String channelCode;

    /**
     * 中间号是否可复用：
     * 为true，如果当日账号下存在有效主被叫绑定关系（绑定中），则返回之前的绑定关系和中间号，并携带过期时间
     */
    private boolean reusable = false;

    private String associationId;

    public String getChannelCode() {
        if (StringUtils.isEmpty(this.telX)) {
            return MidNumType.ZY_HDH;
        } else {
            return channelCode;
        }
    }

    public void setExpiration(Long expiration) {
        if (expiration == null) {
            this.expiration = 0L;
        } else {
            this.expiration = expiration;
        }
    }

}
