package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.mapper.ProjectMapper;
import com.m7.abs.admin.service.IProjectService;
import com.m7.abs.common.domain.entity.ProjectEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements IProjectService {

}
