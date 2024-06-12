package com.fancy.module.common.api.content;

import com.fancy.common.pojo.CommonResult;
import com.fancy.module.common.api.content.Dto.CmsMerchantReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "content-cmsMerchant-rest", url = "${feignUrl.content}")
public interface CmsMerchantApi {

    @PostMapping("/cmsMerchantConfig/callback/agentCreateMerchant")
    CommonResult<Long> agentCreateMerchant(@RequestBody CmsMerchantReqDto cmsMerchantConfigReqDto);

}
