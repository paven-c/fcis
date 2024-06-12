package com.fancy.module.agent.controller;


import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.agent.service.AgMerchantService;
import com.fancy.module.agent.controller.req.EditAgMerchantReq;
import com.fancy.module.agent.controller.req.QueryAgMerchantReq;
import com.fancy.module.agent.controller.vo.AgMerchantVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return CommonResult.success(agMerchantService.pageListMerchant(req));
    }

}
