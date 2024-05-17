package com.m7.abs.api.mapstruct;

import com.m7.abs.api.domain.dto.flashSm.FlashSmDeliveryDispatcherResultDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmDeliveryResultDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmDispatcherDto;
import com.m7.abs.api.domain.dto.flashSm.FlashSmTranslateDto;
import com.m7.abs.common.domain.dto.FlashSmDeliveryReportResultDto;
import com.m7.abs.common.domain.dto.FlashSmReportDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-18T16:13:19+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_362 (Amazon.com Inc.)"
)
@Component
public class FlashSmsMapStructMapperImpl implements FlashSmsMapStructMapper {

    @Override
    public FlashSmDispatcherDto convertFlashSmTranslateDtoToFlashSmDispatcherDto(FlashSmTranslateDto dto) {
        if ( dto == null ) {
            return null;
        }

        FlashSmDispatcherDto flashSmDispatcherDto = new FlashSmDispatcherDto();

        flashSmDispatcherDto.setTaskId( dto.getTaskId() );
        flashSmDispatcherDto.setDeliveryResult( flashSmDeliveryResultDtoListToFlashSmDeliveryDispatcherResultDtoList( dto.getDeliveryResult() ) );

        return flashSmDispatcherDto;
    }

    @Override
    public FlashSmReportDto convertFlashSmTranslateDtoToFlashSmReportDto(FlashSmTranslateDto dto) {
        if ( dto == null ) {
            return null;
        }

        FlashSmReportDto.FlashSmReportDtoBuilder flashSmReportDto = FlashSmReportDto.builder();

        flashSmReportDto.taskId( dto.getTaskId() );
        flashSmReportDto.deliveryResult( flashSmDeliveryResultDtoListToFlashSmDeliveryReportResultDtoList( dto.getDeliveryResult() ) );

        return flashSmReportDto.build();
    }

    protected FlashSmDeliveryDispatcherResultDto flashSmDeliveryResultDtoToFlashSmDeliveryDispatcherResultDto(FlashSmDeliveryResultDto flashSmDeliveryResultDto) {
        if ( flashSmDeliveryResultDto == null ) {
            return null;
        }

        FlashSmDeliveryDispatcherResultDto flashSmDeliveryDispatcherResultDto = new FlashSmDeliveryDispatcherResultDto();

        flashSmDeliveryDispatcherResultDto.setTarget( flashSmDeliveryResultDto.getTarget() );
        flashSmDeliveryDispatcherResultDto.setStatus( flashSmDeliveryResultDto.getStatus() );
        flashSmDeliveryDispatcherResultDto.setMsg( flashSmDeliveryResultDto.getMsg() );

        return flashSmDeliveryDispatcherResultDto;
    }

    protected List<FlashSmDeliveryDispatcherResultDto> flashSmDeliveryResultDtoListToFlashSmDeliveryDispatcherResultDtoList(List<FlashSmDeliveryResultDto> list) {
        if ( list == null ) {
            return null;
        }

        List<FlashSmDeliveryDispatcherResultDto> list1 = new ArrayList<FlashSmDeliveryDispatcherResultDto>( list.size() );
        for ( FlashSmDeliveryResultDto flashSmDeliveryResultDto : list ) {
            list1.add( flashSmDeliveryResultDtoToFlashSmDeliveryDispatcherResultDto( flashSmDeliveryResultDto ) );
        }

        return list1;
    }

    protected FlashSmDeliveryReportResultDto flashSmDeliveryResultDtoToFlashSmDeliveryReportResultDto(FlashSmDeliveryResultDto flashSmDeliveryResultDto) {
        if ( flashSmDeliveryResultDto == null ) {
            return null;
        }

        FlashSmDeliveryReportResultDto.FlashSmDeliveryReportResultDtoBuilder flashSmDeliveryReportResultDto = FlashSmDeliveryReportResultDto.builder();

        flashSmDeliveryReportResultDto.target( flashSmDeliveryResultDto.getTarget() );
        flashSmDeliveryReportResultDto.status( flashSmDeliveryResultDto.getStatus() );
        flashSmDeliveryReportResultDto.msg( flashSmDeliveryResultDto.getMsg() );

        return flashSmDeliveryReportResultDto.build();
    }

    protected List<FlashSmDeliveryReportResultDto> flashSmDeliveryResultDtoListToFlashSmDeliveryReportResultDtoList(List<FlashSmDeliveryResultDto> list) {
        if ( list == null ) {
            return null;
        }

        List<FlashSmDeliveryReportResultDto> list1 = new ArrayList<FlashSmDeliveryReportResultDto>( list.size() );
        for ( FlashSmDeliveryResultDto flashSmDeliveryResultDto : list ) {
            list1.add( flashSmDeliveryResultDtoToFlashSmDeliveryReportResultDto( flashSmDeliveryResultDto ) );
        }

        return list1;
    }
}
