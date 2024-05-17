package com.m7.abs.admin.domain.dto;

import com.m7.abs.common.domain.entity.AccountEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountPageDTO extends AccountEntity {
    private String enterpriseName;
}
