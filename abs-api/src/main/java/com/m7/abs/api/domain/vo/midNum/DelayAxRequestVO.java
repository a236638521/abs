package com.m7.abs.api.domain.vo.midNum;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * AXB,XB延期请求参数
 *
 * @author Kejie Peng
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DelayAxRequestVO {
    /**
     * AXB或者XB绑定关系id:此ID为ABS系统绑定ID
     */
    @NotEmpty
    private String mappingId;
    /**
     * 绑定有效期变更长度，单位为天，这里的值为正整数。
     * 订单当前有效期时间+延长时间=新的有效期时间
     * 最大值29200
     * 注：extraTime的值为延期的增量时间。例如：延期7天，则填写7
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 29200)
    private Integer extraTime;

    /**
     * 绑定关系ID:此ID为第三方通道绑定ID,和mappingId关联
     */
    private String associationId;

    private String telX;
}
