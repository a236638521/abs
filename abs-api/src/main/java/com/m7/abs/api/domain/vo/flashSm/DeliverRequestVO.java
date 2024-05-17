package com.m7.abs.api.domain.vo.flashSm;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliverRequestVO {
    @NotEmpty
    @Size(max = 40)
    private String account;

    @NotEmpty
    @Size(max = 40)
    private String templateNum;

    /**
     * 投递对象,上限为10000
     */
    @NotEmpty
    @Size(max = 10000)
    private List<DeliverContentVO> deliveryList;
    /**
     * 第三方置顶的通知地址
     */
    @Size(max = 250)
    private String notifyUrl;
}
