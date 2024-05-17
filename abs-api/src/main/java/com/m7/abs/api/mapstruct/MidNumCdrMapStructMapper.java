package com.m7.abs.api.mapstruct;

import com.m7.abs.api.domain.dto.midNum.MidNumRecordDto;
import com.m7.abs.api.domain.dto.midNum.MidNumTranslateDto;
import com.m7.abs.api.mapstruct.conversion.TypeConversionWorker;
import com.m7.abs.common.domain.dto.MiddleNumberCdrReport;
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
    /**
     * convert MiddleNumberCdrEntity To MiddleNumberCdrExcel
     *
     * @param dto
     * @return
     */
    MiddleNumberCdrReport convertCdrEntityToCdrReport(MiddleNumberCdrEntity dto);

    /**
     * convert MidNumTranslateDto To MidNumRecordDto
     * @param dto
     * @return
     */
    MidNumRecordDto convertMidNumTranslateDtoToMidNumRecordDto(MidNumTranslateDto dto);
}
