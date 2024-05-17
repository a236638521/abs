package com.m7.abs.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.dto.FlashSmReportDTO;
import com.m7.abs.admin.domain.vo.report.ReportSearchVO;
import com.m7.abs.admin.mapper.FlashSmCdrReportMapper;
import com.m7.abs.common.domain.entity.FlashSmCdrReportEntity;
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
 * @since 2023-04-10
 */
@Service
public class FlashSmCdrReportServiceImpl extends ServiceImpl<FlashSmCdrReportMapper, FlashSmCdrReportEntity> implements IFlashSmCdrReportService {
    @Autowired
    private FlashSmCdrReportMapper flashSmCdrReportMapper;

    @Override
    public List<FlashSmReportDTO> search(ReportSearchVO searchVO) {
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
        LambdaQueryWrapper<FlashSmCdrReportEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlashSmCdrReportEntity::getAccountId, searchVO.getAccountId());
        wrapper.ge(FlashSmCdrReportEntity::getDateTime, searchVO.getBeginTime());
        wrapper.lt(FlashSmCdrReportEntity::getDateTime, searchVO.getEndTime());
        if (timeType.equals("date")) {
            return flashSmCdrReportMapper.selectReportByHour(wrapper);
        } else if (timeType.equals("month") || timeType.equals("week")) {
            return flashSmCdrReportMapper.selectReportByDay(wrapper);
        } else if (timeType.equals("year")) {
            return flashSmCdrReportMapper.selectReportByMonth(wrapper);
        }
        return null;
    }
}
