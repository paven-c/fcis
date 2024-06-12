package com.fancy.module.common.api.user;

import com.fancy.module.common.api.user.dto.UserRespDTO;
import com.fancy.module.common.api.user.dto.UserSaveReqDTO;
import com.fancy.module.common.convert.user.UserConvert;
import com.fancy.module.common.repository.pojo.dept.Dept;
import com.fancy.module.common.repository.pojo.user.User;
import com.fancy.module.common.service.dept.DeptService;
import com.fancy.module.common.service.user.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 用户API实现类
 *
 * @author paven
 */
@Service
@Validated
public class UserApiImpl implements UserApi {

    @Resource
    private UserService userService;
    @Resource
    private DeptService deptService;

    @Override
    public Long createUser(UserSaveReqDTO createReqDTO) {
        return userService.createUser(UserConvert.INSTANCE.convert(createReqDTO));
    }

    @Override
    public UserRespDTO getUser(Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return null;
        }
        Dept dept = deptService.getDept(user.getDeptId());
        return UserConvert.INSTANCE.convertDTO(user, dept);
    }

}
