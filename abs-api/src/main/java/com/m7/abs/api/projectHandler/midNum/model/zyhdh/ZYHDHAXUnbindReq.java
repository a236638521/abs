package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class ZYHDHAXUnbindReq {
    /**
     * 绑定ID
     */
    private String bindId;
    private String appId;
    /**
     * 小号解绑后的冷却时间：
     * 单位天，在此时间内，小号不会回到号码池中被再次绑定。小号的冷却时间会有一个默认值。解绑时若指定的参数coolDonw，则按照指定的冷却时间处理；否则使用默认冷却时间。0代表不进行冷却。
     * 冷却时间以整天为准，例如冷却时间点指定为"20161020"，则实际冷却时间点为"2016-10-20 23:59:59"
     * 最大值为：29200
     */
    private int coolDown;
}

