package com.m7.abs.admin.domain.dto;

import com.m7.abs.common.domain.entity.AccountChannelConfEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountChannelConfPageDTO extends AccountChannelConfEntity {
    private String accountName;
    private String channelName;
}
