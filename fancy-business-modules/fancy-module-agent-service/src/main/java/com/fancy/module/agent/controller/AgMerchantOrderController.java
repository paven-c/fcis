package com.fancy.module.agent.controller;


import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.controller.req.EditAgMerchantOrderReq;
import com.fancy.module.agent.controller.req.QueryAgMerchantOrderReq;
import com.fancy.module.agent.service.AgMerchantOrderService;
import com.fancy.module.agent.controller.vo.AgMerchantOrderOverviewVo;
import com.fancy.module.agent.controller.vo.AgMerchantOrderVo;
import com.fancy.module.common.api.permission.PermissionApi;
import com.fancy.module.common.enums.permission.RoleCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.fancy.component.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * <p>
 * 客户订单 前端控制器
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/merchantOrder")
public class AgMerchantOrderController {

    @Resource
    private AgMerchantOrderService agMerchantOrderservice;

    @Resource
    private PermissionApi permissionApi;
    @PostMapping("/overview")
    public CommonResult<AgMerchantOrderOverviewVo> overview(){
        Set<String> userRoleCodeListByUserIds = permissionApi.getUserRoleCodeListByUserIds(getLoginUserId());
        //是否代理商角色
        boolean b = userRoleCodeListByUserIds.stream().anyMatch(s -> RoleCodeEnum.getAgent().contains(s));
        List<Long> creatorIds =b ? Collections.singletonList(getLoginUserId()) : null;
        return CommonResult.success(agMerchantOrderservice.overview(creatorIds));
    }

    @PostMapping("/pageList")
    public CommonResult<PageResult<AgMerchantOrderVo>> pageList(@RequestBody QueryAgMerchantOrderReq req){
        Set<String> userRoleCodeListByUserIds = permissionApi.getUserRoleCodeListByUserIds(getLoginUserId());
        //是否代理商角色
        boolean b = userRoleCodeListByUserIds.stream().anyMatch(s -> RoleCodeEnum.getAgent().contains(s));
        req.setCreatorIds(b ? Collections.singletonList(getLoginUserId()) : null);
        return CommonResult.success(agMerchantOrderservice.pageList(req));
    }

    @PostMapping("/info")
    public CommonResult<AgMerchantOrderVo> info(@RequestBody QueryAgMerchantOrderReq req){
        return CommonResult.success(agMerchantOrderservice.info(req));
    }

    @PostMapping("/add")
    public CommonResult<?> add(@RequestBody EditAgMerchantOrderReq req){
        agMerchantOrderservice.add(req);
        return CommonResult.success("成功");
    }

}
