package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.vo.enterprise.EnterpriseSaveOrUpdateVO;
import com.m7.abs.admin.mapper.EnterpriseConfMapper;
import com.m7.abs.admin.mapper.EnterpriseMapper;
import com.m7.abs.admin.service.IEnterpriseService;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.EnterpriseConfEntity;
import com.m7.abs.common.domain.entity.EnterpriseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2022-02-28
 */
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, EnterpriseEntity> implements IEnterpriseService {
    @Autowired
    private EnterpriseConfMapper enterpriseConfMapper;

    @Override
    public BaseResponse enterpriseSaveOrUpdate(EnterpriseSaveOrUpdateVO record) {
        EnterpriseEntity enterpriseEntity = record.getEnterpriseEntity();
        EnterpriseConfEntity enterpriseConfEntity = record.getEnterpriseConfEntity();

        boolean insert = enterpriseEntity.getId() == null ? true : false;
        boolean b = this.saveOrUpdate(enterpriseEntity);


        if (b) {
            if (insert) {
                EnterpriseConfEntity confEntity = EnterpriseConfEntity.builder()
                        .enterpriseId(enterpriseEntity.getId())
                        .build();

                if (enterpriseConfEntity != null) {
                    confEntity.setOssProxyEnable(enterpriseConfEntity.getOssProxyEnable());
                    confEntity.setOssPushFilePathOnlyEnable(enterpriseConfEntity.getOssPushFilePathOnlyEnable());
                    confEntity.setStorageConfList(enterpriseConfEntity.getStorageConfList());
                }
                enterpriseConfMapper.insert(confEntity);
            } else {
                EnterpriseConfEntity conf = enterpriseConfMapper.selectById(enterpriseEntity.getId());
                if (conf == null) {
                    conf = EnterpriseConfEntity.builder()
                            .enterpriseId(enterpriseEntity.getId())
                            .ossProxyEnable(enterpriseConfEntity.getOssProxyEnable())
                            .ossPushFilePathOnlyEnable(enterpriseConfEntity.getOssPushFilePathOnlyEnable())
                            .build();
                    conf.setStorageConfList(enterpriseConfEntity.getStorageConfList());
                    enterpriseConfMapper.insert(conf);
                } else {
                    if (enterpriseConfEntity != null) {
                        conf.setOssProxyEnable(enterpriseConfEntity.getOssProxyEnable());
                        conf.setOssPushFilePathOnlyEnable(enterpriseConfEntity.getOssPushFilePathOnlyEnable());
                        conf.setStorageConfList(enterpriseConfEntity.getStorageConfList());
                    }
                    enterpriseConfMapper.updateById(conf);
                }

            }


        }
        return null;
    }
}
