package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.AccountChannelConfPageDTO;
import com.m7.abs.common.domain.entity.AccountChannelConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 账户通道关系表 Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Mapper
@Repository
@DS("abs_config")
public interface AccountChannelConfMapper extends BaseMapper<AccountChannelConfEntity> {

    IPage<AccountChannelConfPageDTO> findByPage(PageBean page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
