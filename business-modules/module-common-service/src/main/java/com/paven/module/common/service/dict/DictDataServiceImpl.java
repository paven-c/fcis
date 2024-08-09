package com.paven.module.common.service.dict;


import static com.paven.common.exception.util.ServiceExceptionUtil.exception;
import static com.paven.module.common.enums.ErrorCodeConstants.DICT_DATA_NOT_ENABLE;
import static com.paven.module.common.enums.ErrorCodeConstants.DICT_DATA_NOT_EXISTS;
import static com.paven.module.common.enums.ErrorCodeConstants.DICT_DATA_VALUE_DUPLICATE;
import static com.paven.module.common.enums.ErrorCodeConstants.DICT_TYPE_NOT_ENABLE;
import static com.paven.module.common.enums.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.paven.common.enums.CommonStatusEnum;
import com.paven.common.pojo.PageResult;
import com.paven.common.util.collection.CollectionUtils;
import com.paven.common.util.object.BeanUtils;
import com.paven.module.common.api.dict.dto.DictDataDTO;
import com.paven.module.common.controller.dict.vo.data.DictDataPageReqVO;
import com.paven.module.common.controller.dict.vo.data.DictDataSaveReqVO;
import com.paven.module.common.convert.dict.DictDataConvert;
import com.paven.module.common.repository.mapper.dict.DictDataMapper;
import com.paven.module.common.repository.pojo.dict.DictData;
import com.paven.module.common.repository.pojo.dict.DictType;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 字典数据 Service 实现类
 *
 * @author paven
 */
@Service
@Slf4j
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictData> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(DictData::getDictType)
            .thenComparingInt(DictData::getSort);

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private DictDataMapper dictDataMapper;

    @Override
    public List<DictData> getDictDataList(Integer status, String dictType) {
        List<DictData> list = dictDataMapper.selectListByStatusAndDictType(status, dictType);
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public PageResult<DictData> getDictDataPage(DictDataPageReqVO pageReqVO) {
        return dictDataMapper.selectPage(pageReqVO);
    }

    @Override
    public DictData getDictData(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    public Long createDictData(DictDataSaveReqVO createReqVO) {
        // 校验字典类型有效
        validateDictTypeExists(createReqVO.getDictType());
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(null, createReqVO.getDictType(), createReqVO.getValue());

        // 插入字典类型
        DictData dictData = BeanUtils.toBean(createReqVO, DictData.class);
        dictDataMapper.insert(dictData);
        return dictData.getId();
    }

    @Override
    public void updateDictData(DictDataSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDictDataExists(updateReqVO.getId());
        // 校验字典类型有效
        validateDictTypeExists(updateReqVO.getDictType());
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(updateReqVO.getId(), updateReqVO.getDictType(), updateReqVO.getValue());

        // 更新字典类型
        DictData updateObj = BeanUtils.toBean(updateReqVO, DictData.class);
        dictDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictData(Long id) {
        // 校验是否存在
        validateDictDataExists(id);

        // 删除字典数据
        dictDataMapper.deleteById(id);
    }

    @Override
    public long getDictDataCountByDictType(String dictType) {
        return dictDataMapper.selectCountByDictType(dictType);
    }

    @VisibleForTesting
    public void validateDictDataValueUnique(Long id, String dictType, String value) {
        DictData dictData = dictDataMapper.selectByDictTypeAndValue(dictType, value);
        if (dictData == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
        if (!dictData.getId().equals(id)) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
    }

    @VisibleForTesting
    public void validateDictDataExists(Long id) {
        if (id == null) {
            return;
        }
        DictData dictData = dictDataMapper.selectById(id);
        if (dictData == null) {
            throw exception(DICT_DATA_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    public void validateDictTypeExists(String type) {
        DictType dictType = dictTypeService.getDictType(type);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw exception(DICT_TYPE_NOT_ENABLE);
        }
    }

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        Map<String, DictData> dictDataMap = CollectionUtils.convertMap(
                dictDataMapper.selectByDictTypeAndValues(dictType, values), DictData::getValue);
        // 校验
        values.forEach(value -> {
            DictData dictData = dictDataMap.get(value);
            if (dictData == null) {
                throw exception(DICT_DATA_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dictData.getStatus())) {
                throw exception(DICT_DATA_NOT_ENABLE, dictData.getLabel());
            }
        });
    }

    @Override
    public DictData getDictData(String dictType, String value) {
        return dictDataMapper.selectByDictTypeAndValue(dictType, value);
    }

    @Override
    public DictData parseDictData(String dictType, String label) {
        return dictDataMapper.selectByDictTypeAndLabel(dictType, label);
    }

    @Override
    public List<DictData> getDictDataListByDictType(List<String> dictTypes) {
        if (CollUtil.isEmpty(dictTypes)) {
            return Lists.newArrayList();
        }
        List<DictData> list = dictDataMapper.selectList(Wrappers.lambdaQuery(DictData.class)
                .in(DictData::getDictType, dictTypes));
        list.sort(Comparator.comparing(DictData::getSort));
        return list;
    }

    @Override
    public List<DictData> getFieldDictDataList(List<Long> fieldIds) {
        if (CollUtil.isEmpty(fieldIds)) {
            return Lists.newArrayList();
        }
        return dictDataMapper.selectList(Wrappers.lambdaQuery(DictData.class)
                .in(DictData::getId, fieldIds));
    }

    @Override
    public void insertBatch(List<DictDataDTO> dictDataList) {
        if (CollUtil.isEmpty(dictDataList)) {
            return;
        }
        dictDataMapper.insertBatch(DictDataConvert.INSTANCE.convertList(dictDataList));
    }

    @Override
    public void updateBatch(List<DictDataDTO> dictDataList) {
        if (CollUtil.isEmpty(dictDataList)) {
            return;
        }
        dictDataMapper.updateBatch(DictDataConvert.INSTANCE.convertList(dictDataList));
    }

    @Override
    public void deleteBatchIds(Set<Long> dicDataIds) {
        if (CollUtil.isEmpty(dicDataIds)) {
            return;
        }
        dictDataMapper.deleteBatchIds(dicDataIds);
    }

}