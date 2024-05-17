package com.m7.abs.admin.domain.dto;

import com.m7.abs.common.domain.entity.FlashSmTemplateEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlashSmTemplatePageDTO extends FlashSmTemplateEntity {
    private String accountName;
    private String channelName;
}
