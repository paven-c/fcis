package com.paven.module.common.api.dict;

import com.paven.common.util.object.BeanUtils;
import com.paven.module.common.api.dict.dto.DictDataDTO;
import com.paven.module.common.repository.pojo.dict.DictData;
import com.paven.module.common.service.dict.DictDataService;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * 字典数据 API 实现类
 *
 * @author paven
 */
@Service
public class DictDataApiImpl implements DictDataApi {

    @Resource
    private DictDataService dictDataService;

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        dictDataService.validateDictDataList(dictType, values);
    }

    @Override
    public DictDataDTO getDictData(String dictType, String value) {
        DictData dictData = dictDataService.getDictData(dictType, value);
        return BeanUtils.toBean(dictData, DictDataDTO.class);
    }

    @Override
    public DictDataDTO parseDictData(String dictType, String label) {
        DictData dictData = dictDataService.parseDictData(dictType, label);
        return BeanUtils.toBean(dictData, DictDataDTO.class);
    }

    @Override
    public List<DictDataDTO> getDictDataList(List<String> dictTypes) {
        List<DictData> list = dictDataService.getDictDataListByDictType(dictTypes);
        return BeanUtils.toBean(list, DictDataDTO.class);
    }

    @Override
    public List<DictDataDTO> getFieldDictDataList(List<Long> fieldIds) {
        List<DictData> list = dictDataService.getFieldDictDataList(fieldIds);
        return BeanUtils.toBean(list, DictDataDTO.class);
    }

    @Override
    public void insertBatch(List<DictDataDTO> dictDataList) {
        dictDataService.insertBatch(dictDataList);
    }

    @Override
    public void updateBatch(List<DictDataDTO> dictDataList) {
        dictDataService.updateBatch(dictDataList);
    }

    @Override
    public void deleteBatchIds(Set<Long> dicDataIds) {
        dictDataService.deleteBatchIds(dicDataIds);
    }

}
