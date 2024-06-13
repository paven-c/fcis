package com.fancy.module.common.api.user;

import com.fancy.module.common.api.user.dto.UserRespDTO;
import com.fancy.module.common.api.user.dto.UserSaveReqDTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 用户API
 *
 * @author paven
 */
public interface UserApi {

    /**
     * 创建用户
     *
     * @param reqDTO 用户信息
     * @return 用户编号
     */
    Long createUser(@Valid UserSaveReqDTO reqDTO);

    /**
     * 更新用户
     *
     * @param reqDTO 用户信息
     */
    void updateUser(UserSaveReqDTO reqDTO);

    /**
     * 获得用户信息
     *
     * @param userId 用户编号
     * @return 用户信息
     */
    UserRespDTO getUser(Long userId);

    /**
     * 获取用户信息
     * @param userIds 用户编号
     * @return 用户信息
     */
    List<UserRespDTO> getUserByIds(List<Long> userIds);
}
