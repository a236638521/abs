package com.m7.abs.api.mapstruct;

import com.m7.abs.api.domain.dto.midNum.MidNumSmsDispatcherDto;
import com.m7.abs.api.domain.dto.midNum.MidNumSmsTranslateDto;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-18T16:13:19+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_362 (Amazon.com Inc.)"
)
@Component
public class MidNumSmsMapStructMapperImpl implements MidNumSmsMapStructMapper {

    @Override
    public MidNumSmsDispatcherDto convertMidNumSmsTranslateToDispatcher(MidNumSmsTranslateDto dto) {
        if ( dto == null ) {
            return null;
        }

        MidNumSmsDispatcherDto midNumSmsDispatcherDto = new MidNumSmsDispatcherDto();

        midNumSmsDispatcherDto.setCaller( dto.getCaller() );
        midNumSmsDispatcherDto.setCallee( dto.getCallee() );
        midNumSmsDispatcherDto.setTelX( dto.getTelX() );
        midNumSmsDispatcherDto.setSmsResult( dto.getSmsResult() );
        midNumSmsDispatcherDto.setSmsNumber( dto.getSmsNumber() );
        midNumSmsDispatcherDto.setSmsTime( dto.getSmsTime() );

        return midNumSmsDispatcherDto;
    }
}
