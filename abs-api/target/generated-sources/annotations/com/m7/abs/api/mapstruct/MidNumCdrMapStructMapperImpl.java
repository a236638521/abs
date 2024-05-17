package com.m7.abs.api.mapstruct;

import com.m7.abs.api.domain.dto.midNum.MidNumRecordDto;
import com.m7.abs.api.domain.dto.midNum.MidNumTranslateDto;
import com.m7.abs.common.domain.dto.MiddleNumberCdrReport;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-18T16:13:19+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_362 (Amazon.com Inc.)"
)
@Component
public class MidNumCdrMapStructMapperImpl implements MidNumCdrMapStructMapper {

    @Override
    public MiddleNumberCdrReport convertCdrEntityToCdrReport(MiddleNumberCdrEntity dto) {
        if ( dto == null ) {
            return null;
        }

        MiddleNumberCdrReport middleNumberCdrReport = new MiddleNumberCdrReport();

        middleNumberCdrReport.setAccountId( dto.getAccountId() );
        middleNumberCdrReport.setId( dto.getId() );
        middleNumberCdrReport.setMappingId( dto.getMappingId() );
        middleNumberCdrReport.setCaller( dto.getCaller() );
        middleNumberCdrReport.setTelX( dto.getTelX() );
        middleNumberCdrReport.setCallee( dto.getCallee() );
        middleNumberCdrReport.setResult( dto.getResult() );
        middleNumberCdrReport.setCallRecording( dto.isCallRecording() );
        middleNumberCdrReport.setBillDuration( dto.getBillDuration() );
        middleNumberCdrReport.setRateDuration( dto.getRateDuration() );
        middleNumberCdrReport.setBeginTime( dto.getBeginTime() );
        middleNumberCdrReport.setAlertingTime( dto.getAlertingTime() );
        middleNumberCdrReport.setConnectTime( dto.getConnectTime() );
        middleNumberCdrReport.setReleaseTime( dto.getReleaseTime() );
        middleNumberCdrReport.setRecordFileProxy( dto.getRecordFileProxy() );
        middleNumberCdrReport.setRecordFileHost( dto.getRecordFileHost() );
        middleNumberCdrReport.setRecordFilePath( dto.getRecordFilePath() );
        middleNumberCdrReport.setCallerCarrier( dto.getCallerCarrier() );
        middleNumberCdrReport.setCalledCarrier( dto.getCalledCarrier() );

        return middleNumberCdrReport;
    }

    @Override
    public MidNumRecordDto convertMidNumTranslateDtoToMidNumRecordDto(MidNumTranslateDto dto) {
        if ( dto == null ) {
            return null;
        }

        MidNumRecordDto midNumRecordDto = new MidNumRecordDto();

        midNumRecordDto.setCaller( dto.getCaller() );
        midNumRecordDto.setCallee( dto.getCallee() );
        midNumRecordDto.setTelX( dto.getTelX() );
        midNumRecordDto.setResult( dto.getResult() );
        midNumRecordDto.setCallDisplay( dto.getCallDisplay() );
        midNumRecordDto.setCallerShow( dto.getCallerShow() );
        midNumRecordDto.setCalledShow( dto.getCalledShow() );
        midNumRecordDto.setCallerArea( dto.getCallerArea() );
        midNumRecordDto.setCalledArea( dto.getCalledArea() );
        midNumRecordDto.setCallerCarrier( dto.getCallerCarrier() );
        midNumRecordDto.setCalledCarrier( dto.getCalledCarrier() );
        midNumRecordDto.setCallRecording( dto.isCallRecording() );
        midNumRecordDto.setBillDuration( dto.getBillDuration() );
        midNumRecordDto.setBeginTime( dto.getBeginTime() );
        midNumRecordDto.setAlertingTime( dto.getAlertingTime() );
        midNumRecordDto.setConnectTime( dto.getConnectTime() );
        midNumRecordDto.setReleaseTime( dto.getReleaseTime() );
        midNumRecordDto.setRecordFileUrl( dto.getRecordFileUrl() );
        midNumRecordDto.setReleaseDir( dto.getReleaseDir() );

        return midNumRecordDto;
    }
}
