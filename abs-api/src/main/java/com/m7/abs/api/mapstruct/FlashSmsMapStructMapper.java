package com.m7.abs.api.mapstruct;

import com.m7.abs.api.domain.dto.flashSm.FlashSmDispatcherDto;
import com.m7.abs.common.domain.dto.FlashSmReportDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmTranslateDto;
import com.m7.abs.api.mapstruct.conversion.TypeConversionWorker;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.stereotype.Component;

/**
 * @author kejie peng
 */
@Component
@Mapper(componentModel = ComponentModel.SPRING, uses = TypeConversionWorker.class)
public interface FlashSmsMapStructMapper {
    /**
     * convert FlashSmTranslateDto To FlashSmDispatcherDto
     *
     * @param dto
     * @return
     */
    FlashSmDispatcherDto convertFlashSmTranslateDtoToFlashSmDispatcherDto(FlashSmTranslateDto dto);
    /**
     * convert FlashSmTranslateDto To FlashSmReportDto
     *
     * @param dto
     * @return
     */
    FlashSmReportDto convertFlashSmTranslateDtoToFlashSmReportDto(FlashSmTranslateDto dto);
}
