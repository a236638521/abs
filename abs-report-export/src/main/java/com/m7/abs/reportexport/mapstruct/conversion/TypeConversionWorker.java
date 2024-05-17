package com.m7.abs.reportexport.mapstruct.conversion;

import com.m7.abs.common.utils.DateUtil;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author damomo
 */
@Component
public class TypeConversionWorker {
    @Named("timestampToDate")
    public Date timestampToDate(String timestamp) {
        return DateUtil.parseTimeToDate(timestamp);
    }
}
