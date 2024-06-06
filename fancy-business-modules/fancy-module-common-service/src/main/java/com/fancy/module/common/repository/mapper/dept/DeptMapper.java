package com.fancy.module.common.repository.mapper.dept;

import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.common.controller.admin.dept.vo.dept.DeptListReqVO;
import com.fancy.module.common.repository.pojo.dept.Dept;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author paven
 */
@Mapper
public interface DeptMapper extends BaseMapperX<Dept> {

    default List<Dept> selectList(DeptListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<Dept>()
                .likeIfPresent(Dept::getDeptName, reqVO.getName())
                .eqIfPresent(Dept::getStatus, reqVO.getStatus()));
    }

    default Dept selectByParentIdAndName(Long parentId, String name) {
        return selectOne(Dept::getParentId, parentId, Dept::getDeptName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(Dept::getParentId, parentId);
    }

    default List<Dept> selectListByParentId(Collection<Long> parentIds) {
        return selectList(Dept::getParentId, parentIds);
    }

}
