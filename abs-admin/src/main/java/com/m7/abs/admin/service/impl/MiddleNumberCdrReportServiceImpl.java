package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.dto.MidNumReportDTO;
import com.m7.abs.admin.domain.vo.report.ReportSearchVO;
import com.m7.abs.admin.mapper.MiddleNumberCdrReportMapper;
import com.m7.abs.admin.service.IMiddleNumberCdrReportService;
import com.m7.abs.common.domain.entity.MiddleNumberCdrReportEntity;
import com.m7.abs.common.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2023-04-03
 */
@Service
public class MiddleNumberCdrReportServiceImpl extends ServiceImpl<MiddleNumberCdrReportMapper, MiddleNumberCdrReportEntity> implements IMiddleNumberCdrReportService {
    @Autowired
    private MiddleNumberCdrReportMapper middleNumberCdrReportMapper;

    @Override
    public List<MidNumReportDTO> search(ReportSearchVO searchVO) {
        String timeType = searchVO.getTimeType();
        Date timeValue = searchVO.getTimeValue();

        Date startTime = null;
        Date endTime = null;
        switch (timeType) {
            case "year":
                startTime = DateUtil.getYearStartTime(timeValue, "GMT+8");
                endTime = DateUtil.getYearEndTime(timeValue, "GMT+8");
                break;
            case "month":
                startTime = DateUtil.getMonthStartTime(timeValue, "GMT+8");
                endTime = DateUtil.getMonthEndTime(timeValue, "GMT+8");
                break;
            case "week":
                startTime = DateUtil.getWeekStartTime(timeValue, "GMT+8");
                endTime = DateUtil.getWeekEndTime(timeValue, "GMT+8");
                break;
            case "date":
                startTime = DateUtil.getDayStartTime(timeValue, "GMT+8");
                endTime = DateUtil.getDayEndTime(timeValue, "GMT+8");
                break;
        }
        searchVO.setBeginTime(DateUtil.parseDateToStr(startTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
        searchVO.setEndTime(DateUtil.parseDateToStr(endTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
        LambdaQueryWrapper<MiddleNumberCdrReportEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MiddleNumberCdrReportEntity::getAccountId, searchVO.getAccountId());
        wrapper.ge(MiddleNumberCdrReportEntity::getDateTime, searchVO.getBeginTime());
        wrapper.lt(MiddleNumberCdrReportEntity::getDateTime, searchVO.getEndTime());
        if (timeType.equals("date")) {
            return middleNumberCdrReportMapper.selectReportByHour(wrapper);
        } else if (timeType.equals("month") || timeType.equals("week")) {
            return middleNumberCdrReportMapper.selectReportByDay(wrapper);
        } else if (timeType.equals("year")) {
            return middleNumberCdrReportMapper.selectReportByMonth(wrapper);
        }
        return null;
    }
}
