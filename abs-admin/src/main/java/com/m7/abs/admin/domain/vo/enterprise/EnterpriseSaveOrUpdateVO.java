package com.m7.abs.admin.domain.vo.enterprise;

import com.m7.abs.common.domain.entity.EnterpriseConfEntity;
import com.m7.abs.common.domain.entity.EnterpriseEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnterpriseSaveOrUpdateVO {
    private EnterpriseEntity enterpriseEntity;
    private EnterpriseConfEntity enterpriseConfEntity;
}
