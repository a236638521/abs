package com.m7.abs.support.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.common.domain.entity.MiddleNumberBindLogBackupEntity;
import com.m7.abs.support.mapper.MiddleNumberBindLogBackupMapper;
import com.m7.abs.support.service.IMiddleNumberBindLogBackupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-21
 */
@Slf4j
@Service
public class MiddleNumberBindLogBackupServiceImpl extends ServiceImpl<MiddleNumberBindLogBackupMapper, MiddleNumberBindLogBackupEntity> implements IMiddleNumberBindLogBackupService {
}
