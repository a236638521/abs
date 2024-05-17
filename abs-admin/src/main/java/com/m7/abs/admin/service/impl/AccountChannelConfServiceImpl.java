package com.m7.abs.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.m7.abs.admin.domain.bean.PageBean;
import com.m7.abs.admin.domain.dto.AccountChannelConfPageDTO;
import com.m7.abs.admin.mapper.AccountChannelConfMapper;
import com.m7.abs.admin.mapper.MiddleNumberPoolMapper;
import com.m7.abs.admin.service.IAccountChannelConfService;
import com.m7.abs.common.domain.base.BaseResponse;
import com.m7.abs.common.domain.entity.AccountChannelConfEntity;
import com.m7.abs.common.domain.entity.MiddleNumberPoolEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 账户通道关系表 服务实现类
 * </p>
 *
 * @author Kejie Peng
 * @since 2021-12-14
 */
@Service
public class AccountChannelConfServiceImpl extends ServiceImpl<AccountChannelConfMapper, AccountChannelConfEntity> implements IAccountChannelConfService {

    @Autowired
    private MiddleNumberPoolMapper middleNumberPoolMapper;

    @Autowired
    private AccountChannelConfMapper accountChannelConfMapper;

    @Override
    public BaseResponse deleteById(AccountChannelConfEntity record) {
        //检查账户是否绑定了该通道小号
        QueryWrapper<MiddleNumberPoolEntity> wrapper = new QueryWrapper<>();//条件
        wrapper.eq("account_id", record.getAccountId());
        wrapper.eq("channel_id", record.getChannelId());
        List<MiddleNumberPoolEntity> middleNumberPoolEntities = middleNumberPoolMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(middleNumberPoolEntities)) {
            return BaseResponse.success(super.removeById(record.getId()));
        }
        return BaseResponse.fail("账户绑定了该通道小号，不允许删除!");
    }

    @Override
    public IPage<AccountChannelConfPageDTO> findByPage(PageBean page, QueryWrapper queryWrapper) {
        return accountChannelConfMapper.findByPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse saveOrUpdateCustom(AccountChannelConfEntity record) {
        if (StringUtils.isEmpty(record.getAccountId())) {
            return BaseResponse.fail("未知账户");
        }
        if (StringUtils.isEmpty(record.getChannelId())) {
            return BaseResponse.fail("未知通道");
        }

        String id = record.getId();
        if (StringUtils.isEmpty(id)) {
            QueryWrapper<AccountChannelConfEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account_id", record.getAccountId());
            queryWrapper.eq("channel_id", record.getChannelId());
            queryWrapper.last("limit 1");
            AccountChannelConfEntity accountChannelConfEntity = accountChannelConfMapper.selectOne(queryWrapper);
            if (accountChannelConfEntity != null) {
                return BaseResponse.fail("此账户已经绑定该通道，请勿重复绑定！");
            }
        }

        boolean b = this.saveOrUpdate(record);
        if (b) {
            return BaseResponse.success();
        } else {
            return BaseResponse.fail("保存失败！");
        }
    }
}
