package com.m7.abs.reportexport.mapstruct;

import com.m7.abs.common.domain.dto.MiddleNumberCdrCsv;
import com.m7.abs.common.domain.dto.MiddleNumberCdrExcel;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import com.m7.abs.reportexport.mapstruct.conversion.TypeConversionWorker;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.stereotype.Component;

/**
 * @author kejie peng
 */
@Component
@Mapper(componentModel = ComponentModel.SPRING, uses = TypeConversionWorker.class)
public interface MidNumCdrMapStructMapper {
    /**
     * convert MiddleNumberCdrEntity To MiddleNumberCdrExcel
     *
     * @param dto
     * @return
     */
    MiddleNumberCdrExcel convertCdrEntityToCdrExcel(MiddleNumberCdrEntity dto);

    /**
     * convert MiddleNumberCdrEntity To MiddleNumberCdrCsv
     *
     * @param dto
     * @return
     */
    MiddleNumberCdrCsv convertCdrEntityToCdrCsv(MiddleNumberCdrEntity dto);
}
