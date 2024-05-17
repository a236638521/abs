package com.m7.abs.api.domain.vo.midNum;

import lombok.*;

import javax.validation.constraints.NotNull;
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
public class GroupAddNumbersRespVO {

    /**
     * 已处理的号码
     */
    private List<String> success;
    /**
     * 失败的号码
     */
    private List<String> fail;

 }
