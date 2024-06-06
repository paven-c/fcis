package com.fancy.module.common.service.dict;


import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_DATA_NOT_ENABLE;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_DATA_NOT_EXISTS;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_DATA_VALUE_DUPLICATE;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_TYPE_NOT_ENABLE;
import static com.fancy.module.common.enums.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;

import cn.hutool.core.collection.CollUtil;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.pojo.PageResult;
import com.fancy.common.util.collection.CollectionUtils;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.module.common.controller.admin.dict.vo.data.DictDataPageReqVO;
import com.fancy.module.common.controller.admin.dict.vo.data.DictDataSaveReqVO;
import com.fancy.module.common.repository.mapper.dict.DictDataMapper;
import com.fancy.module.common.repository.pojo.dict.DictData;
import com.fancy.module.common.repository.pojo.dict.DictType;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 字典数据 Service 实现类
 *
 * @author paven
 */
@Service
@Slf4j
public class DictDataServiceImpl implements DictDataService {

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
    public List<DictData> getDictDataListByDictType(String dictType) {
        List<DictData> list = dictDataMapper.selectList(DictData::getDictType, dictType);
        list.sort(Comparator.comparing(DictData::getSort));
        return list;
    }

}
