package com.m7.abs.api.domain.vo.midNum;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * 
 *
 * @author hulin
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupAddNumbersRequestVO {
    @NotEmpty
    @Size(max = 40)
    private String account;
    /**
     * 分组Id
     */
    @NotEmpty
    @Size(max = 64)
    private String groupId;
    /**
     * 号码列表
     */
    @NotNull
    @Size(max = 100)
    private List<String> numbers;

    private String associationId;
 }
