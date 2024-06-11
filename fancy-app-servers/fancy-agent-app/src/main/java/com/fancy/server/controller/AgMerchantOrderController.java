package com.fancy.server.controller;


import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.module.common.req.QueryAgMerchantOrderReq;
import com.fancy.module.common.service.AgMerchantOrderService;
import com.fancy.module.common.vo.AgMerchantOrderOverviewVo;
import com.fancy.module.common.vo.AgMerchantOrderVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/overview")
    public CommonResult<AgMerchantOrderOverviewVo> overview(){
        return CommonResult.success(null);
    }

    @PostMapping("/pageList")
    public CommonResult<PageResult<AgMerchantOrderVo>> pageList(@RequestBody QueryAgMerchantOrderReq req){
        return CommonResult.success(null);
    }

}
