package com.m7.abs.api.domain.vo.midNum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 小号绑定返回
 *
 * @author hulin
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BindResponseVO {

    /**
     * 绑定关系id，我们系统的id
     */
    @NotEmpty
    private String mappingId;
    /**
     * 接收运营商返回的中间号
     */
    private String telX;
    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;
}
