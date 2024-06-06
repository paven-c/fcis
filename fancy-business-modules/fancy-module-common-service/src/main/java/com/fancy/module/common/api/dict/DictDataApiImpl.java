package com.fancy.module.common.api.dict;

import com.fancy.common.util.object.BeanUtils;
import com.fancy.module.common.api.dict.dto.DictDataRespDTO;
import com.fancy.module.common.repository.pojo.dict.DictData;
import com.fancy.module.common.service.dict.DictDataService;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;
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
    public DictDataRespDTO getDictData(String dictType, String value) {
        DictData dictData = dictDataService.getDictData(dictType, value);
        return BeanUtils.toBean(dictData, DictDataRespDTO.class);
    }

    @Override
    public DictDataRespDTO parseDictData(String dictType, String label) {
        DictData dictData = dictDataService.parseDictData(dictType, label);
        return BeanUtils.toBean(dictData, DictDataRespDTO.class);
    }

    @Override
    public List<DictDataRespDTO> getDictDataList(String dictType) {
        List<DictData> list = dictDataService.getDictDataListByDictType(dictType);
        return BeanUtils.toBean(list, DictDataRespDTO.class);
    }

}
