package com.m7.abs.api.domain.vo.midNum;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 * AX绑定请求参数
 *
 * @author hulin
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BindAXRequestVO {
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
     * 被叫号码
     */
    @NotEmpty
    @Size(max = 20)
    private String telA;

    /**
     * 被叫外显号码 "0"-显真实号码 "1"-显示中间号 默认 "1"
     */
    @Builder.Default
    private Integer icDisplayFlag = 1;

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

    public String getChannelCode() {
        if (StringUtils.isEmpty(this.telX)) {
            return "ZY_HDH";
        } else {
            return channelCode;
        }
    }

    /*public Long getExpiration() {
        if (expiration == null || !(expiration > 0 && expiration < (15 * 24 * 3600))) {
            expiration = Long.valueOf(15 * 24 * 3600);
        }
        return expiration;
    }*/
}
