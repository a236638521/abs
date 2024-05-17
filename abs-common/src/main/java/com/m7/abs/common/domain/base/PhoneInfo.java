package com.m7.abs.common.domain.base;

import com.m7.abs.common.constant.common.CarrierTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneInfo {
    /**
     *  区域编码 Locale : HK, US, CN ...
     */
    private String regionCode;
    /**
     * 国号: 86, 1, 852 ... @link: https://blog.csdn.net/wzygis/article/details/45073327
     */
    private int countryCode;
    /**
     * 去掉+号 和 国号/区号 后的实际号码
     */
    private long nationalNumber;
    /**
     * 所在地区描述信息
     */
    private String description;
    /**
     * 发去掉+号后的号码 (用于阿里云送短信)
     */
    private String number;
    /**
     * 完整号码
     */
    private String fullNumber;

    /**
     * 运营商
     */
    private CarrierTypeEnum carrier;
}
