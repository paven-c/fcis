package com.fancy.module.common.repository.mapper.dict;

import com.fancy.common.pojo.PageResult;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.common.controller.admin.dict.vo.type.DictTypePageReqVO;
import com.fancy.module.common.repository.pojo.dict.DictType;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author paven
 */
@Mapper
public interface DictTypeMapper extends BaseMapperX<DictType> {

    default PageResult<DictType> selectPage(DictTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DictType>()
                .likeIfPresent(DictType::getName, reqVO.getName())
                .likeIfPresent(DictType::getType, reqVO.getType())
                .eqIfPresent(DictType::getStatus, reqVO.getStatus())
                .betweenIfPresent(DictType::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DictType::getId));
    }

    default DictType selectByType(String type) {
        return selectOne(DictType::getType, type);
    }

    default DictType selectByName(String name) {
        return selectOne(DictType::getName, name);
    }

    @Update("UPDATE system_dict_type SET deleted = 1, deleted_time = #{deletedTime} WHERE id = #{id}")
    void updateToDelete(@Param("id") Long id, @Param("deletedTime") LocalDateTime deletedTime);

}
