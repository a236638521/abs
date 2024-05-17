package com.m7.abs.admin.domain.vo.account;

import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.common.domain.entity.AccountFlashSmConfEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountInsertVO {
    private AccountEntity accountEntity;
    private List<AccountFlashSmConfEntity> flashSmConfList;
}
