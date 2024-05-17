package com.m7.abs.api.projectHandler.midNum.model.zyhdh;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class ZYHDHGGroupReq {
    private String appId;
    /**
     * 号码集合ID:
     * 商户侧生成，并保持取值唯一性， 参数长度大于0小于等于20位，取值范围仅支持0-9、A-Z、a-z。
     * 参数生成规则：分配给商户的platformid + 其他位。
     * 说明：为保持兼容性，请勿仅使用大小写区分参数。
     */
    private String groupId;
    /**
     * 操作类型：
     * 0：新增号码集合；
     * 1：删除号码集合；
     * 2：清空号码集合；
     * 3：查询集合包含的号码列表；
     * 4：查询一个号码的归属集合groupId；
     * 6：全量覆盖（响应消息需回传号码组全量号码,增强一致性校验）；
     * 7:将一个号码设置为接听来电的唯一
     * 号码。
     * 8：取消将呼叫转接到唯一号码设置。
     * 注释：目前仅支持1、6。
     */
    @JSONField(name = "Method")
    private String Method;
    /**
     * 需要操作的号码列表：
     * 数组形式，
     * 手机号格式：
     * 格式遵循国际电信联盟定义的E.164标准
     * 手机号前加86，如：8613511112222
     * 固话格式：
     * 1.固话前加86加区号（首位0不写），如：北京固话 861082325588；
     * 2.固话前加区号（首位0必写），如：北京固话01082325588
     * （非手机号和固话号码，需加白名单处理后才可使用）
     * 特服格式：
     * 1.特服号码前加不带86不带区号，保持原样
     * 如：4003008000,95568；
     * 2.特服号码前加86不带区号，
     * 如：864003008000,8695568；
     * 3.特服号码前不加86加区号(首位0必写)，
     * 如：0104003008000,01095568；
     * 4.特服号码前加86加区号(首位0不写)，
     * 如：86104003008000,861095568；
     * 12539号码：
     * 1.号码前不加86，如：1253913511112222
     * 2.号码前加86，如：861253913511112222
     */
    private List<String> numberList;
    /**
     * 需要通信服务类型：
     * 0： 仅语音；
     * 1： 仅短信；
     * 2： 语音和短信；
     * 参数说明： 根据该参数决定平台分配的中间号能提供的通信服务种类，目前仅支持语音功能。
     */
    private String srvType;
}
