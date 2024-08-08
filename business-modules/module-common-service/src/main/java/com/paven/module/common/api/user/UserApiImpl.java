package com.paven.module.common.api.user;

import com.paven.module.common.api.user.dto.UserRespDTO;
import com.paven.module.common.api.user.dto.UserSaveReqDTO;
import com.paven.module.common.controller.user.vo.user.UserUpdateReqVO;
import com.paven.module.common.convert.user.UserConvert;
import com.paven.module.common.repository.pojo.dept.Dept;
import com.paven.module.common.repository.pojo.user.User;
import com.paven.module.common.service.dept.DeptService;
import com.paven.module.common.service.user.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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
    public void updateUser(UserSaveReqDTO reqDTO) {
        userService.updateUser(UserConvert.INSTANCE.convertVO(reqDTO));
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

    @Override
    public List<UserRespDTO> getUserByIds(List<Long> userIds) {
        List<User> userList = userService.getUserList(userIds);
        return UserConvert.INSTANCE.convertDtoList(userList);
    }

}
