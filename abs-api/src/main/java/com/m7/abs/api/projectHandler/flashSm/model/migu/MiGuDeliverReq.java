package com.m7.abs.api.projectHandler.flashSm.model.migu;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class MiGuDeliverReq {
    /**
     * 1 使用通知式USSD发送(5S超时回执)（废弃）
     * 2 使用闪信发送(不需要回执)
     * 3 通知式USSD投递失败时改由闪信投递(不需要回执)（废弃）
     * 4 使用闪信发送，需要回执结果(120S超时回执)
     * 5 通知式USSD投递失败时改由闪信投递，需要回执结果(120S超时回执)（废弃）
     * 6 短信(120S超时回执)
     * 7 交互式USSD（废弃）
     * 8使用通知式USSD发送(无域查询) (5S超时回执)（废弃）
     * 9挂机彩信
     * 10挂机增彩
     * 11 使用通知式USSD发送(5S超时回执)（非基于通话）（废弃）
     * 12 使用闪信发送(不需要回执)（非基于通话）（废弃）
     * 13 通知式USSD投递失败时改由闪信投递(不需要回执)（非基于通话）（废弃）
     * 14 使用闪信发送，需要回执结果(120S超时回执)（非基于通话）（废弃）
     * 15 通知式USSD投递失败时改由闪信投递，需要回执结果(120S超时回执)（非基于通话）（废弃）
     * 16 短信(120S超时回执)（非基于通话）（废弃）
     * 17 交互式USSD（非基于通话）（废弃）
     * 18使用通知式USSD发送(无域查询) (5S超时回执)（非基于通话）（废弃）
     * 19挂机彩信（非基于通话）（废弃）
     * 20挂机增彩（非基于通话）（废弃）
     * 注意：1.msgtype的取值必须要被包含在模板的deliverytype中；2.建议该字段取值和模板提交时scene字段取值能够匹配，比如：1)、1|2|3|4|5|8:匹配模板提交时的scene=1|3；
     * 2)、6：匹配模板提交时的scene=5；
     */
    private String msgtype;
    /**
     * 发送号码
     * 请填10658086
     */
    private String src;
    /**
     * 业务类型：
     * 1-模板投递
     */
    private String biztype;
    /**
     * 第三方指定的通知地址。
     * 如无此字段，则根据能力开放平台配置为准；如果有此字段，以此字段为准。
     */
    private String notifyurl;
    /**
     * 接收对象、模板类型编号和参数，目前只会有一个内容
     */
    private List<MiGuDeliverContent> content;
}
