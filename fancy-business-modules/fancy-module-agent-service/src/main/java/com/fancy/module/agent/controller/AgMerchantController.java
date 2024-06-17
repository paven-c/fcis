package com.fancy.module.agent.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.controller.vo.AuthTokenGetReqVO;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.service.AgMerchantService;
import com.fancy.module.agent.controller.req.EditAgMerchantReq;
import com.fancy.module.agent.controller.req.QueryAgMerchantReq;
import com.fancy.module.agent.controller.vo.AgMerchantVo;
import com.fancy.module.common.api.content.CmsAuthApi;
import com.fancy.module.common.api.content.CmsMerchantApi;
import com.fancy.module.common.api.permission.PermissionApi;
import com.fancy.module.common.enums.permission.RoleCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import java.util.Collection;
import java.util.Objects;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.fancy.common.pojo.CommonResult.success;
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
    @Resource
    private CmsAuthApi cmsAuthApi;

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

    @PostMapping("/updateAuthStatus")
    @PermitAll
    public CommonResult<?> updateAuthStatus(@RequestBody EditAgMerchantReq req){
        agMerchantService.updateAuthStatus(req);
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


    @GetMapping("/loginByMerchantId")
    @Operation(summary = "根据MerchantId登录")
    public CommonResult<AuthTokenGetReqVO> loginByMerchantId(@RequestParam Long merchantId) {
        Assert.notNull(merchantId, "merchantId不能为空");
        AuthTokenGetReqVO authTokenGetReqVO = new AuthTokenGetReqVO();
        // 转换成fancy
        List<AgMerchant> agMerchants = agMerchantService.list(Wrappers.lambdaQuery(AgMerchant.class)
                .eq(AgMerchant::getId, merchantId)
                .eq(AgMerchant::getDeleted, 0)
        );
        Assert.notEmpty(agMerchants, "代理商不存在");
        AgMerchant agMerchant = agMerchants.get(0);
        Long fancyMerchantId = agMerchant.getMerchantId();
        Assert.notNull(fancyMerchantId, "该代理商没有对应fancy商户");
        // 过滤白牌
        if (!Objects.equals(agMerchant.getMerchantType(), "cp")) {
            authTokenGetReqVO.setCode(3);
            return success(authTokenGetReqVO);
        }
        CommonResult<String> result = cmsAuthApi.loginByMerchantId(fancyMerchantId);
        if (result.getCode() == 200) {
            authTokenGetReqVO.setCode(0);
            authTokenGetReqVO.setToken(result.getData());
            return success(authTokenGetReqVO);
        } else {
            authTokenGetReqVO.setCode(1);
            return success(authTokenGetReqVO);
        }
    }



}
