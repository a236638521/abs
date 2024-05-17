package com.m7.abs.admin.mapstruct;

import com.m7.abs.admin.mapstruct.conversion.TypeConversionWorker;
import com.m7.abs.common.domain.dto.MiddleNumberCdrExcel;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.stereotype.Component;

/**
 * @author kejie peng
 */
@Component
@Mapper(componentModel = ComponentModel.SPRING, uses = TypeConversionWorker.class)
public interface MidNumCdrMapStructMapper {
}
