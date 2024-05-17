package com.m7.abs.api.controller;

import com.m7.abs.api.common.annotation.Authenticate;
import com.m7.abs.api.domain.vo.midNum.*;
import com.m7.abs.api.service.IMidNumGroupService;
import com.m7.abs.common.annotation.WebAspect;
import com.m7.abs.common.domain.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 小号绑定相关
 *
 * @author hulin
 */
@Slf4j
@RestController
@RequestMapping("/midNum/group")
public class MidNumGroupController {
    private final IMidNumGroupService midNumGroupService;

    @Autowired
    public MidNumGroupController(IMidNumGroupService midNumGroupService) {
        this.midNumGroupService = midNumGroupService;
    }


    /**
     * gxb绑定
     *
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->group->gxbBind")
    @PostMapping(value = "/gxb/bind")
    public BaseResponse gxbBind(@RequestBody @Valid BindGXBRequestVO requestVO) {
        return midNumGroupService.gxbBind(requestVO);
    }

    /**
     * gxb解绑
     *
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->group->gxbUnBind")
    @PostMapping(value = "/gxb/unBind")
    public BaseResponse gxbUnBind(@RequestBody @Valid UnBindRequestVO requestVO) {
        return midNumGroupService.gxbUnBind(requestVO);
    }

    /**
     * 创建分组
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->group->createGroup")
    @PostMapping(value = "/createGroup")
    public BaseResponse<GroupCreateRespVO> createGroup(@RequestBody @Valid GroupCreateRequestVO requestVO) {
        return midNumGroupService.createGroup(requestVO);
    }

    /**
     * 删除分组,清空号码
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->group->deleteGroup")
    @PostMapping(value = "/delGroup")
    public BaseResponse deleteGroup(@RequestBody @Valid GroupDelRequestVO requestVO) {
        return midNumGroupService.deleteGroup(requestVO);
    }

    /**
     * 全量查询号码组
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->group->numberList")
    @PostMapping(value = "/numberList")
    public BaseResponse numberList(@RequestBody @Valid GroupQueryNumberListRequestVO requestVO) {
        return midNumGroupService.numberList(requestVO);
    }

    /**
     * 新增号码
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->group->addNumbers")
    @PostMapping(value = "/addNumbers")
    public BaseResponse<GroupAddNumbersRespVO> addNumbers(@RequestBody @Valid GroupAddNumbersRequestVO requestVO) {
        return midNumGroupService.addNumbers(requestVO);
    }

    /**
     * 删除号码
     * @param requestVO
     * @return
     */
    @Authenticate(permission = true, checkIp = true)
    @WebAspect(injectReqId = true, logDesc = "api->midNum->group->delNumbers")
    @PostMapping(value = "/delNumbers")
    public BaseResponse<GroupDelNumbersRespVO> delNumbers(@RequestBody @Valid GroupDelNumbersRequestVO requestVO) {
        return midNumGroupService.delNumbers(requestVO);
    }

}
