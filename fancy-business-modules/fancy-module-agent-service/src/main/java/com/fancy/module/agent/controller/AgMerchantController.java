package com.fancy.module.agent.controller;


import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.service.AgMerchantService;
import com.fancy.module.agent.controller.req.EditAgMerchantReq;
import com.fancy.module.agent.controller.req.QueryAgMerchantReq;
import com.fancy.module.agent.controller.vo.AgMerchantVo;
import com.fancy.module.common.api.permission.PermissionApi;
import com.fancy.module.common.enums.permission.RoleCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.fancy.component.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * <p>
 * 客户管理 前端控制器
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/merchant")
public class AgMerchantController {

    @Resource
    private AgMerchantService agMerchantService;
    @Resource
    private PermissionApi permissionApi;

    @PostMapping("/add")
    public CommonResult<?> addMerchant(@RequestBody EditAgMerchantReq req){
        agMerchantService.addMerchant(req);
        return CommonResult.success("成功");
    }

    @PostMapping("/update")
    public CommonResult<?> updateMerchant(@RequestBody EditAgMerchantReq req){
        agMerchantService.updateMerchant(req);
        return CommonResult.success("成功");
    }

    @PostMapping("/pageList")
    public CommonResult<PageResult<AgMerchantVo>> pageListMerchant(@RequestBody QueryAgMerchantReq req){
        Set<String> userRoleCodeListByUserIds = permissionApi.getUserRoleCodeListByUserIds(getLoginUserId());
        //是否代理商角色
        boolean b = userRoleCodeListByUserIds.stream().anyMatch(s -> RoleCodeEnum.getAgent().contains(s));
        req.setCreatorIds(b ? Collections.singletonList(getLoginUserId()) : null);
        return CommonResult.success(agMerchantService.pageListMerchant(req));
    }

    @PostMapping("/list")
    public CommonResult<List<AgMerchantVo>> listMerchant(@RequestBody QueryAgMerchantReq req){
        Set<String> userRoleCodeListByUserIds = permissionApi.getUserRoleCodeListByUserIds(getLoginUserId());
        //是否代理商角色
        boolean b = userRoleCodeListByUserIds.stream().anyMatch(s -> RoleCodeEnum.getAgent().contains(s));
        req.setCreatorIds(b ? Collections.singletonList(getLoginUserId()) : null);
        return CommonResult.success(agMerchantService.listMerchant(req));
    }


}
