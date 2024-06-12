package com.fancy.module.common.api.user;

import com.fancy.module.common.api.user.dto.UserRespDTO;
import com.fancy.module.common.api.user.dto.UserSaveReqDTO;
import jakarta.validation.Valid;

/**
 * 用户API
 *
 * @author paven
 */
public interface UserApi {

    /**
     * 创建用户
     *
     * @param createReqDTO 用户信息
     * @return 用户编号
     */
    Long createUser(@Valid UserSaveReqDTO createReqDTO);

    /**
     * 获得用户信息
     *
     * @param userId 用户编号
     * @return 用户信息
     */
    UserRespDTO getUser(Long userId);
}
