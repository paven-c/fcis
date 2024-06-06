package com.fancy.server.controller;

import com.fancy.common.pojo.CommonResult;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author paven
 */
@RestController
public class TestController {

    @PermitAll
    @RequestMapping("/test")
    public CommonResult<String> test() {
        return CommonResult.success("OK");
    }

}
