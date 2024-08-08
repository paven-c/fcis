package com.paven.module.common.controller.user;


import static com.paven.common.exception.util.ServiceExceptionUtil.exception;
import static com.paven.common.pojo.CommonResult.success;
import static com.paven.component.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.paven.module.common.enums.ErrorCodeConstants.FILE_IS_EMPTY;

import com.google.common.collect.Lists;
import com.paven.common.pojo.CommonResult;
import com.paven.module.common.controller.user.vo.profile.UserProfileRespVO;
import com.paven.module.common.controller.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.paven.module.common.controller.user.vo.profile.UserProfileUpdateReqVO;
import com.paven.module.common.convert.user.UserConvert;
import com.paven.module.common.repository.pojo.user.User;
import com.paven.module.common.service.dept.DeptService;
import com.paven.module.common.service.permission.PermissionService;
import com.paven.module.common.service.permission.RoleService;
import com.paven.module.common.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author paven
 */
@Tag(name = "用户个人中心")
@RestController
@RequestMapping("/user/profile")
@Validated
@Slf4j
public class UserProfileController {

    @Resource
    private UserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;

    @GetMapping("/me")
    @Operation(summary = "获得登录用户信息")
    public CommonResult<UserProfileRespVO> getUserProfile() {
        // 获得用户基本信息
        User user = userService.getUser(getLoginUserId());
//        // 获得用户角色
//        List<Role> userRoles = roleService.getRoleListFromCache(permissionService.getUserRoleIdListByUserId(user.getId()));
//        // 获得部门信息
//        Dept dept = user.getDeptId() != null ? deptService.getDept(user.getDeptId()) : null;
        return success(UserConvert.INSTANCE.convert(user, Lists.newArrayList(), null));
    }

    @PutMapping("/update")
    @Operation(summary = "修改用户个人信息")
    public CommonResult<Boolean> updateUserProfile(@Valid @RequestBody UserProfileUpdateReqVO reqVO) {
        userService.updateUserProfile(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "修改用户个人密码")
    public CommonResult<Boolean> updateUserProfilePassword(@Valid @RequestBody UserProfileUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(getLoginUserId(), reqVO);
        return success(true);
    }

    @RequestMapping(value = "/update-avatar", method = {RequestMethod.POST, RequestMethod.PUT})
    @Operation(summary = "上传用户个人头像")
    public CommonResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String avatar = userService.updateUserAvatar(getLoginUserId(), file.getInputStream());
        return success(avatar);
    }

}
