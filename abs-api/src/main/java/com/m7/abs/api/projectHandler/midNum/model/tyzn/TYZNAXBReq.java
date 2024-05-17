package com.m7.abs.api.projectHandler.midNum.model.tyzn;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @program: abs
 * @description:
 * @author: yx
 * @create: 2022-02-15 15:54
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class TYZNAXBReq {
    /**
     * A号码
     */
    private String telA;
    /**
     * 虚拟号码
     */
    private String telX;
    /**
     * B号码
     */
    private String telB;
    /**
     * 区号，传递X号码时可不传，传递时后选择该区号的X号码
     */
    private String areaCode;
    /**
     * 按键放音编码，必须包含 3 个场景的编 码。
     * 按照“A->X,B->X,其他号码 ->X”的顺序填写编码，编码之间 以逗号分隔。比如:“1,2,3”表示 A->X 放音编 号为 1，
     * B->X放音编号为 2，其他号码->X 放音编号为 3。放音编码由平台生成
     */
    private String anucode;
    /**
     * 绑定过期时间，单位秒，0代表永久绑定。默认永久绑定
     */
    private String expiration;
    /**
     * 针对AXB中的A或者B作为主叫时，是否在被叫上显示 来话的真实号码。0:不显示真实号码 1:显示真实号码,
     * 来显控制按照“A->B 时B上的显示,B->A 时在 A 上的显示” 的顺序填写编码，编码之间以逗号分隔。
     * 默认为 0,0(不 显示真实号码)。
     */
    private String callDisplay;
    /**
     * 录音控制，0：不开通 1：开通，默认0，不开通
     */
    private String callRecording;
    /**
     * 自定义字段
     */
    private String remark;


}
