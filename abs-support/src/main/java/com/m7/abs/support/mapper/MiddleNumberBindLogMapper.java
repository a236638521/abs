package com.m7.abs.support.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
@Mapper
@Repository
public interface MiddleNumberBindLogMapper extends BaseMapper<MiddleNumberBindLogEntity> {

    /**
     * 获取已经过期的绑定记录
     * 状态为 BINDING 并且创建时间到现在的时间差大于过期时间
     *
     * @param limit 查询条数
     * @return
     */
    List<MiddleNumberBindLogEntity> getExpiredLogs(@Param(value = "limit") int limit);

    /**
     * 获取已经失效的记录
     * 状态为 UNBIND EXPIRED,并且lastUpdateTime距离当前时间超过7天的数据
     *
     * @param timeLimit 保留期限,单位为秒
     * @param limit     查询条数
     * @return
     */
    List<MiddleNumberBindLogEntity> getInvalidLogs(@Param(value = "timeLimit") int timeLimit, @Param(value = "limit") int limit);

    /**
     * 批量更新记录
     * @param list
     * @return
     */
    boolean updateBatchById(List<MiddleNumberBindLogEntity> list);
}
