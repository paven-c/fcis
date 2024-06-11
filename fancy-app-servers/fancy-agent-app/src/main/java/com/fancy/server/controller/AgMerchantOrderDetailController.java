package com.fancy.server.controller;


import com.fancy.common.pojo.CommonResult;
import com.fancy.module.common.repository.dto.MerchantTaskNumListDTO;
import com.fancy.module.common.repository.vo.MerchantTaskNumListVO;
import com.fancy.module.common.service.AgMerchantOrderDetailService;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 内容服务子表 前端控制器
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/agMerchantOrderDetail")
public class AgMerchantOrderDetailController {


    @Resource
    private AgMerchantOrderDetailService agMerchantOrderDetailService;

    @PostMapping("/listMerchantTaskNum")
    public CommonResult<List<MerchantTaskNumListVO>> listMerchantTaskNum(@RequestBody MerchantTaskNumListDTO merchantTaskNumListDTO) {
        return CommonResult.success(agMerchantOrderDetailService.listMerchantTaskNum(merchantTaskNumListDTO));
    }

}
