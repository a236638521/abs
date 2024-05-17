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
public class ZYHDHDelayReq {
    /**
     * 绑定ID：
     * 唯一标示一组绑定关系的ID，和多号平台接收绑定请求时生成。
     */
    private String bindId;
    /**
     * 应用Id，标志出要操作的应用。
     * 若账户名下只有一个应用，本字段可选填。若不填此字段，则操作针对该应用。
     * 若账户名下有多个应用，本字段必填，否则会报错
     */
    private String appId;
    /**
     * 绑定有效期变更长度。单位为秒。这里的值为正数。
     * 订单当前有效期时间 +延长时间 = 新的有效期时间。
     * 最大值为：4294967296
     */
    private int delta;
}
