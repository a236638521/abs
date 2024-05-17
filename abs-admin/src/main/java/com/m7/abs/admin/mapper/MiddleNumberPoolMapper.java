package com.m7.abs.admin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.m7.abs.admin.domain.bean.ConditionBean;
import com.m7.abs.admin.domain.dto.MiddleNumberPoolPageDTO;
import com.m7.abs.common.domain.entity.MiddleNumberPoolEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 小号池 Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Mapper
@Repository
@DS("abs_config")
public interface MiddleNumberPoolMapper extends BaseMapper<MiddleNumberPoolEntity> {

    IPage<MiddleNumberPoolPageDTO> findByPage(Page page, @Param("conditions") List<ConditionBean> conditionList);
}
