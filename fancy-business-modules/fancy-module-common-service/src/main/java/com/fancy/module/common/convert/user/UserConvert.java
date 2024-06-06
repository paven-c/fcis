package com.fancy.module.common.convert.user;

import com.fancy.common.util.collection.CollectionUtils;
import com.fancy.common.util.collection.MapUtils;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.module.common.controller.admin.dept.vo.dept.DeptSimpleRespVO;
import com.fancy.module.common.controller.admin.permission.vo.role.RoleSimpleRespVO;
import com.fancy.module.common.controller.admin.user.vo.profile.UserProfileRespVO;
import com.fancy.module.common.controller.admin.user.vo.user.UserRespVO;
import com.fancy.module.common.controller.admin.user.vo.user.UserSimpleRespVO;
import com.fancy.module.common.repository.pojo.dept.Dept;
import com.fancy.module.common.repository.pojo.permission.Role;
import com.fancy.module.common.repository.pojo.user.User;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

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
