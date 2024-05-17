package com.m7.abs.api.service;

import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.common.domain.base.BaseResponse;

/**
 * @author pengkj
 */
public interface IMidNumGroupService {
    BaseResponse gxbBind(BindGXBRequestVO requestVO);

    BaseResponse gxbUnBind(UnBindRequestVO requestVO);

    BaseResponse<GroupCreateRespVO> createGroup(GroupCreateRequestVO requestVO);

    BaseResponse deleteGroup(GroupDelRequestVO requestVO);

    BaseResponse<GroupAddNumbersRespVO> addNumbers(GroupAddNumbersRequestVO requestVO);

    BaseResponse<GroupDelNumbersRespVO> delNumbers(GroupDelNumbersRequestVO requestVO);

    BaseResponse numberList(GroupQueryNumberListRequestVO requestVO);
}
