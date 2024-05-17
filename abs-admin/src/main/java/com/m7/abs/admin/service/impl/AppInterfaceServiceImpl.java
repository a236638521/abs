package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.mapper.AppInterfaceMapper;
import com.m7.abs.admin.service.IAppInterfaceService;
import com.m7.abs.common.domain.entity.AppInterfaceEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 对接接口列表 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-02-28
 */
@Service
public class AppInterfaceServiceImpl extends ServiceImpl<AppInterfaceMapper, AppInterfaceEntity> implements IAppInterfaceService {

}
