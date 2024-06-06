package com.fancy.module.common.repository.mapper.dict;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.mybatis.core.mapper.BaseMapperX;
import com.fancy.component.mybatis.core.query.LambdaQueryWrapperX;
import com.fancy.module.common.controller.admin.dict.vo.data.DictDataPageReqVO;
import com.fancy.module.common.repository.pojo.dict.DictData;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictDataMapper extends BaseMapperX<DictData> {

    default DictData selectByDictTypeAndValue(String dictType, String value) {
        return selectOne(DictData::getDictType, dictType, DictData::getValue, value);
    }

    default DictData selectByDictTypeAndLabel(String dictType, String label) {
        return selectOne(DictData::getDictType, dictType, DictData::getLabel, label);
    }

    default List<DictData> selectByDictTypeAndValues(String dictType, Collection<String> values) {
        return selectList(new LambdaQueryWrapper<DictData>().eq(DictData::getDictType, dictType)
                .in(DictData::getValue, values));
    }

    default long selectCountByDictType(String dictType) {
        return selectCount(DictData::getDictType, dictType);
    }

    default PageResult<DictData> selectPage(DictDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DictData>()
                .likeIfPresent(DictData::getLabel, reqVO.getLabel())
                .eqIfPresent(DictData::getDictType, reqVO.getDictType())
                .eqIfPresent(DictData::getStatus, reqVO.getStatus())
                .orderByDesc(Arrays.asList(DictData::getDictType, DictData::getSort)));
    }

    default List<DictData> selectListByStatusAndDictType(Integer status, String dictType) {
        return selectList(new LambdaQueryWrapperX<DictData>()
                .eqIfPresent(DictData::getStatus, status)
                .eqIfPresent(DictData::getDictType, dictType));
    }

}
