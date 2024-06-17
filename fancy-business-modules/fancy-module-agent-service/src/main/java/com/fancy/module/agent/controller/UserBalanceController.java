package com.fancy.module.agent.controller;


import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.common.pojo.CommonResult.success;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_BALANCE_NOT_EXISTS;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_NOT_EXISTS;

import com.fancy.common.pojo.CommonResult;
import com.fancy.module.agent.controller.vo.UserBalanceRespVO;
import com.fancy.module.agent.convert.balance.AgUserBalanceConvert;
import com.fancy.module.agent.repository.pojo.AgUserBalance;
import com.fancy.module.agent.service.AgUserBalanceService;
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import java.util.Optional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author paven
 */
@Tag(name = "用户账户")
@RestController
@RequestMapping("/user/balance")
@Validated
public class UserBalanceController {

    @Resource
    private AgUserBalanceService userBalanceService;
    @Resource
    private UserApi userApi;

    @Operation(summary = "用户账户")
    @GetMapping("/detail")
//    @PreAuthorize("@ss.hasPermission('agent:user-balance:detail')")
    public CommonResult<UserBalanceRespVO> getUserBalance(@RequestParam("userId") Long userId) {
        UserRespDTO user = Optional.ofNullable(userApi.getUser(userId)).orElseThrow(() -> exception(USER_NOT_EXISTS));
        AgUserBalance userBalance = userBalanceService.getUserBalance(user.getId());
        if (userBalance == null) {
            throw exception(USER_BALANCE_NOT_EXISTS);
        }
        return success(AgUserBalanceConvert.INSTANCE.convertVO(userBalance,user.getNickname()));
    }
}
