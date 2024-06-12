package com.fancy.module.common.service.user;


import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.common.util.collection.CollectionUtils.convertSet;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_EMAIL_EXISTS;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_IMPORT_LIST_IS_EMPTY;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_IS_DISABLE;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_MOBILE_EXISTS;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_NOT_EXISTS;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_PASSWORD_FAILED;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_USERNAME_EXISTS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.exception.ServiceException;
import com.fancy.common.pojo.PageResult;
import com.fancy.common.util.collection.CollectionUtils;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.component.datapermission.core.util.DataPermissionUtils;
import com.fancy.module.common.api.file.FileApi;
import com.fancy.module.common.controller.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.fancy.module.common.controller.user.vo.profile.UserProfileUpdateReqVO;
import com.fancy.module.common.controller.user.vo.user.UserImportExcelVO;
import com.fancy.module.common.controller.user.vo.user.UserImportRespVO;
import com.fancy.module.common.controller.user.vo.user.UserPageReqVO;
import com.fancy.module.common.controller.user.vo.user.UserSaveReqVO;
import com.fancy.module.common.repository.mapper.user.UserMapper;
import com.fancy.module.common.repository.pojo.dept.Dept;
import com.fancy.module.common.repository.pojo.user.User;
import com.fancy.module.common.service.dept.DeptService;
import com.fancy.module.common.service.permission.PermissionService;
import com.google.common.annotations.VisibleForTesting;
import com.mzt.logapi.context.LogRecordContext;
import jakarta.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 后台用户 Service 实现类
 *
 * @author paven
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${sys.user.init-password:}")
    private String userInitPassword;

    @Resource
    private UserMapper userMapper;
    @Resource
    private DeptService deptService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private FileApi fileApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserSaveReqVO createReqVO) {
        validateUserForCreateOrUpdate(null, createReqVO.getUsername(), createReqVO.getMobile(), createReqVO.getEmail(), createReqVO.getDeptId());
        User user = BeanUtils.toBean(createReqVO, User.class);
        user.setStatus(createReqVO.getStatus() == null ? CommonStatusEnum.ENABLE.getStatus() : createReqVO.getStatus());
        user.setPassword(encodePassword(createReqVO.getPassword()));
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserSaveReqVO updateReqVO) {
        updateReqVO.setPassword(null);
        User oldUser = validateUserForCreateOrUpdate(
                updateReqVO.getId(), updateReqVO.getUsername(), updateReqVO.getMobile(), updateReqVO.getEmail(), updateReqVO.getDeptId());
        User updateObj = BeanUtils.toBean(updateReqVO, User.class);
        userMapper.updateById(updateObj);
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {
        userMapper.updateById(new User().setId(id).setLastLoginIp(loginIp).setLastLoginDate(LocalDateTime.now()));
    }

    @Override
    public void updateUserProfile(Long id, UserProfileUpdateReqVO reqVO) {
        // 校验正确性
        validateUserExists(id);
        validateEmailUnique(id, reqVO.getEmail());
        validateMobileUnique(id, reqVO.getMobile());
        // 执行更新
        userMapper.updateById(BeanUtils.toBean(reqVO, User.class).setId(id));
    }

    @Override
    public void updateUserPassword(Long id, UserProfileUpdatePasswordReqVO reqVO) {
        // 校验旧密码密码
        validateOldPassword(id, reqVO.getOldPassword());
        // 执行更新
        User updateObj = new User().setId(id);
        // 加密密码
        updateObj.setPassword(encodePassword(reqVO.getNewPassword()));
        userMapper.updateById(updateObj);
    }

    @Override
    public String updateUserAvatar(Long id, InputStream avatarFile) {
        validateUserExists(id);
        // 存储文件
        String avatar = fileApi.createFile(IoUtil.readBytes(avatarFile));
        // 更新路径
        User sysUserDO = new User();
        sysUserDO.setId(id);
        sysUserDO.setAvatar(avatar);
        userMapper.updateById(sysUserDO);
        return avatar;
    }

    @Override
    public void updateUserPassword(Long id, String password) {
        User user = validateUserExists(id);
        User updateObj = new User();
        updateObj.setId(id);
        updateObj.setPassword(encodePassword(password));
        userMapper.updateById(updateObj);
        LogRecordContext.putVariable("user", user);
        LogRecordContext.putVariable("newPassword", updateObj.getPassword());
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        // 校验用户存在
        validateUserExists(id);
        // 更新状态
        User updateObj = new User();
        updateObj.setId(id);
        updateObj.setStatus(status);
        userMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        User user = validateUserExists(id);
        userMapper.deleteById(id);
        permissionService.processUserDeleted(id);
        LogRecordContext.putVariable("user", user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    @Override
    public PageResult<User> getUserPage(UserPageReqVO reqVO) {
        return userMapper.selectPage(reqVO, getDeptCondition(reqVO.getDeptId()));
    }

    @Override
    public User getUser(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getUserListByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectListByDeptIds(deptIds);
    }

    @Override
    public List<User> getUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return userMapper.selectBatchIds(ids);
    }

    @Override
    public void validateUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<User> users = userMapper.selectBatchIds(ids);
        Map<Long, User> userMap = CollectionUtils.convertMap(users, User::getId);
        // 校验
        ids.forEach(id -> {
            User user = userMap.get(id);
            if (user == null) {
                throw exception(USER_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
                throw exception(USER_IS_DISABLE, user.getNickname());
            }
        });
    }

    @Override
    public List<User> getUserListByNickname(String nickname) {
        return userMapper.selectListByNickname(nickname);
    }

    /**
     * 获得部门条件：查询指定部门的子部门编号们，包括自身
     *
     * @param deptId 部门编号
     * @return 部门编号集合
     */
    private Set<Long> getDeptCondition(Long deptId) {
        if (deptId == null) {
            return Collections.emptySet();
        }
        Set<Long> deptIds = convertSet(deptService.getChildDeptList(deptId), Dept::getId);
        deptIds.add(deptId);
        return deptIds;
    }

    private User validateUserForCreateOrUpdate(Long id, String username, String mobile, String email, Long deptId) {
        return DataPermissionUtils.executeIgnore(() -> {
            // 校验用户存在
            User user = validateUserExists(id);
            // 校验用户名唯一
            validateUsernameUnique(id, username);
            // 校验手机号唯一
            validateMobileUnique(id, mobile);
            // 校验邮箱唯一
            validateEmailUnique(id, email);
            // 校验部门处于开启状态
//            deptService.validateDeptList(CollectionUtils.singleton(deptId));
            return user;
        });
    }

    @VisibleForTesting
    User validateUserExists(Long id) {
        if (id == null) {
            return null;
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return user;
    }

    @VisibleForTesting
    void validateUsernameUnique(Long id, String username) {
        if (StrUtil.isBlank(username)) {
            return;
        }
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_USERNAME_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_USERNAME_EXISTS);
        }
    }

    @VisibleForTesting
    void validateEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_EMAIL_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_EMAIL_EXISTS);
        }
    }

    @VisibleForTesting
    void validateMobileUnique(Long id, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        User user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_MOBILE_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_MOBILE_EXISTS);
        }
    }

    /**
     * 校验旧密码
     *
     * @param id          用户 id
     * @param oldPassword 旧密码
     */
    @VisibleForTesting
    void validateOldPassword(Long id, String oldPassword) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        if (!isPasswordMatch(oldPassword, user.getPassword())) {
            throw exception(USER_PASSWORD_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserImportRespVO importUserList(List<UserImportExcelVO> importUsers, boolean isUpdateSupport) {
        if (CollUtil.isEmpty(importUsers)) {
            throw exception(USER_IMPORT_LIST_IS_EMPTY);
        }
        UserImportRespVO respVO = UserImportRespVO.builder().createUsernames(new ArrayList<>())
                .updateUsernames(new ArrayList<>()).failureUsernames(new LinkedHashMap<>()).build();
        importUsers.forEach(importUser -> {
            // 校验，判断是否有不符合的原因
            try {
                validateUserForCreateOrUpdate(null, null, importUser.getMobile(), importUser.getEmail(), importUser.getDeptId());
            } catch (ServiceException ex) {
                respVO.getFailureUsernames().put(importUser.getUsername(), ex.getMessage());
                return;
            }
            // 判断如果不存在，在进行插入
            User existUser = userMapper.selectByUsername(importUser.getUsername());
            if (existUser == null) {
                userMapper.insert(BeanUtils.toBean(importUser, User.class).setPassword(encodePassword(userInitPassword)));
                respVO.getCreateUsernames().add(importUser.getUsername());
                return;
            }
            // 如果存在，判断是否允许更新
            if (!isUpdateSupport) {
                respVO.getFailureUsernames().put(importUser.getUsername(), USER_USERNAME_EXISTS.getMsg());
                return;
            }
            User updateUser = BeanUtils.toBean(importUser, User.class);
            updateUser.setId(existUser.getId());
            userMapper.updateById(updateUser);
            respVO.getUpdateUsernames().add(importUser.getUsername());
        });
        return respVO;
    }

    @Override
    public List<User> getUserListByStatus(Integer status) {
        return userMapper.selectListByStatus(status);
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
