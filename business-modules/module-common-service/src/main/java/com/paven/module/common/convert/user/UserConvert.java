package com.paven.module.common.convert.user;

import com.paven.common.util.collection.CollectionUtils;
import com.paven.common.util.collection.MapUtils;
import com.paven.common.util.object.BeanUtils;
import com.paven.module.common.api.user.dto.UserRespDTO;
import com.paven.module.common.api.user.dto.UserSaveReqDTO;
import com.paven.module.common.controller.dept.vo.DeptSimpleRespVO;
import com.paven.module.common.controller.permission.vo.role.RoleSimpleRespVO;
import com.paven.module.common.controller.user.vo.profile.UserProfileRespVO;
import com.paven.module.common.controller.user.vo.user.UserRespVO;
import com.paven.module.common.controller.user.vo.user.UserSaveReqVO;
import com.paven.module.common.controller.user.vo.user.UserSimpleRespVO;
import com.paven.module.common.repository.pojo.dept.Dept;
import com.paven.module.common.repository.pojo.permission.Role;
import com.paven.module.common.repository.pojo.user.User;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserSaveReqVO convert(UserSaveReqDTO createReqDTO);
    List<UserRespDTO> convertDtoList(List<User> users);

    default List<UserRespVO> convertList(List<User> list, Map<Long, Dept> deptMap) {
        return CollectionUtils.convertList(list, user -> convert(user, deptMap.get(user.getDeptId())));
    }

    default UserRespVO convert(User user, Dept dept) {
        UserRespVO userVO = BeanUtils.toBean(user, UserRespVO.class);
        if (dept != null) {
            userVO.setDeptName(dept.getDeptName());
        }
        return userVO;
    }

    default UserRespDTO convertDTO(User user, Dept dept) {
        UserRespDTO userDTO = BeanUtils.toBean(user, UserRespDTO.class);
        if (dept != null) {
            userDTO.setDeptName(dept.getDeptName());
        }
        return userDTO;
    }

    default List<UserSimpleRespVO> convertSimpleList(List<User> list, Map<Long, Dept> deptMap) {
        return CollectionUtils.convertList(list, user -> {
            UserSimpleRespVO userVO = BeanUtils.toBean(user, UserSimpleRespVO.class);
            MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> userVO.setDeptName(dept.getDeptName()));
            return userVO;
        });
    }

    default UserProfileRespVO convert(User user, List<Role> userRoles, Dept dept) {
        UserProfileRespVO userVO = BeanUtils.toBean(user, UserProfileRespVO.class);
        userVO.setRoles(BeanUtils.toBean(userRoles, RoleSimpleRespVO.class));
        userVO.setDept(BeanUtils.toBean(dept, DeptSimpleRespVO.class));
        return userVO;
    }

}
