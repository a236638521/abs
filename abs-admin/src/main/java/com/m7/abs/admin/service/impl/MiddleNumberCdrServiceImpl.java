package com.m7.abs.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.MiddleNumberCdrDTO;
import com.m7.abs.admin.mapper.MiddleNumberCdrMapper;
import com.m7.abs.admin.service.IAccountService;
import com.m7.abs.admin.service.IMiddleNumberCdrService;
import com.m7.abs.common.domain.dto.MiddleNumberCdrExcel;
import com.m7.abs.common.domain.entity.AccountEntity;
import com.m7.abs.common.domain.entity.MiddleNumberCdrEntity;
import com.m7.abs.common.utils.BeanUtil;
import com.m7.abs.common.utils.ExcelUtil;
import com.m7.abs.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-03-16
 */
@Slf4j
@Service
public class MiddleNumberCdrServiceImpl extends ServiceImpl<MiddleNumberCdrMapper, MiddleNumberCdrEntity> implements IMiddleNumberCdrService {
    @Autowired
    private MiddleNumberCdrMapper middleNumberCdrMapper;
    @Autowired
    private IAccountService accountService;

    @Override
    public void exportExcel(PageBean page, HttpServletResponse response) {
        PageBean pageBean = this.page(page, page.getQueryWrapper());
        // 定义excel的页签
        List<MiddleNumberCdrExcel> excels = new ArrayList();

        if (pageBean.getTotal() > 0) {
            List<MiddleNumberCdrEntity> records = pageBean.getRecords();
            for (MiddleNumberCdrEntity entity : records) {
                MiddleNumberCdrExcel middleNumberCdrExcel = new MiddleNumberCdrExcel();
                BeanUtil.setVOToVO(entity, middleNumberCdrExcel);
                excels.add(middleNumberCdrExcel);
            }
        }

        HorizontalCellStyleStrategy styleStrategy = ExcelUtil.getStyleStrategy();

        // 字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 构建写入对象
        ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
        // 这里可以多个WriteSheet对象
        WriteSheet writeSheet = EasyExcel.writerSheet(0, "通话记录").head(MiddleNumberCdrExcel.class)
                .registerWriteHandler(styleStrategy)
                .build();
        // 这里可以写多个write
        excelWriter.write(excels, writeSheet);

        // 写入完成
        excelWriter.finish();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        String fileName = format + "cdr.xlsx";

        try {
            outputStream.writeTo(response.getOutputStream());
            response.setContentType("application/octet-stream");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"; filename*=utf-8''%s", fileName, fileName));
            response.setContentLengthLong(outputStream.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public IPage<MiddleNumberCdrDTO> findMidNumByPage(PageBean page) {
        IPage<MiddleNumberCdrDTO> returnPage = middleNumberCdrMapper.findMidNumByPage(page, page.getQueryWrapper());
        List<MiddleNumberCdrDTO> records = returnPage.getRecords();
        Set<String> accountSet = new HashSet<>();
        if (records != null) {
            accountSet = records.parallelStream().map(MiddleNumberCdrDTO::getAccountId).collect(Collectors.toSet());
        }

        if (accountSet.size() > 0) {
            LambdaQueryWrapper<AccountEntity> queryWrapper = new LambdaQueryWrapper<>();
            Set<String> finalAccountSet = accountSet;
            queryWrapper.and(orWrapper -> {
                orWrapper.in(AccountEntity::getBillAccountId, finalAccountSet);
                orWrapper.or();
                orWrapper.in(AccountEntity::getId, finalAccountSet);
            });
            List<AccountEntity> list = accountService.list(queryWrapper);
            Map<String, String> accountNameMap = new HashMap<>();
            if (list != null) {
                accountNameMap = list.parallelStream().collect(Collectors.toMap(AccountEntity::getBillAccountId, AccountEntity::getName));
                accountNameMap.putAll( list.parallelStream().collect(Collectors.toMap(AccountEntity::getId, AccountEntity::getName)));
            }

            if (accountNameMap.size() > 0) {
                Map<String, String> finalAccountNameMap = accountNameMap;
                records.parallelStream().forEach(cdrDto -> {
                    cdrDto.setAccountName(finalAccountNameMap.get(cdrDto.getAccountId()));
                });
            }
        }
        return returnPage;
    }
}
