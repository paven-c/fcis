package com.paven.module.common.repository.mapper.dept;

import com.paven.component.mybatis.core.mapper.BaseMapperX;
import com.paven.component.mybatis.core.query.LambdaQueryWrapperX;
import com.paven.module.common.controller.dept.vo.DeptListReqVO;
import com.paven.module.common.repository.pojo.dept.Dept;
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
