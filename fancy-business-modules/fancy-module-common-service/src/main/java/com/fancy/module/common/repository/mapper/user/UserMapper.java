package com.fancy.module.common.repository.mapper.user;

import com.fancy.common.pojo.PageResult;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.common.controller.user.vo.user.UserPageReqVO;
import com.fancy.module.common.repository.pojo.user.User;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author paven
 */
@Mapper
public interface UserMapper extends BaseMapperX<User> {

    default User selectByUsername(String username) {
        return selectOne(User::getUsername, username);
    }

    default User selectByEmail(String email) {
        return selectOne(User::getEmail, email);
    }

    default User selectByMobile(String mobile) {
        return selectOne(User::getMobile, mobile);
    }

    default PageResult<User> selectPage(UserPageReqVO reqVO, Collection<Long> deptIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<User>()
                .likeIfPresent(User::getUsername, reqVO.getUsername())
                .likeIfPresent(User::getMobile, reqVO.getMobile())
                .eqIfPresent(User::getStatus, reqVO.getStatus())
                .betweenIfPresent(User::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(User::getDeptId, deptIds)
                .orderByDesc(User::getId));
    }

    default List<User> selectListByNickname(String nickname) {
        return selectList(new LambdaQueryWrapperX<User>().like(User::getNickname, nickname));
    }

    default List<User> selectListByStatus(Integer status) {
        return selectList(User::getStatus, status);
    }

    default List<User> selectListByDeptIds(Collection<Long> deptIds) {
        return selectList(User::getDeptId, deptIds);
    }

}
