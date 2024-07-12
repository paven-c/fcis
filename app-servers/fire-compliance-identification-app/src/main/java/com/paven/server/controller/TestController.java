package com.paven.server.controller;

import com.paven.common.pojo.CommonResult;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author paven
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    @PermitAll
    public CommonResult<String> test() {
        return CommonResult.success("OK");
    }

}
