package com.paven.module.common.api.dict;


import static com.paven.common.util.collection.CollectionUtils.convertList;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.paven.module.common.api.dict.dto.DictDataDTO;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 字典数据 API 接口
 *
 * @author paven
 */
public interface DictDataApi {

    /**
     * 校验字典数据们是否有效。如下情况，视为无效： 1. 字典数据不存在 2. 字典数据被禁用
     *
     * @param dictType 字典类型
     * @param values   字典数据值的数组
     */
    void validateDictDataList(String dictType, Collection<String> values);

    /**
     * 获得指定的字典数据，从缓存中
     *
     * @param type  字典类型
     * @param value 字典数据值
     * @return 字典数据
     */
    DictDataDTO getDictData(String type, String value);

    /**
     * 获得指定的字典标签，从缓存中
     *
     * @param type  字典类型
     * @param value 字典数据值
     * @return 字典标签
     */
    default String getDictDataLabel(String type, Integer value) {
        DictDataDTO dictData = getDictData(type, String.valueOf(value));
        if (ObjUtil.isNull(dictData)) {
            return StrUtil.EMPTY;
        }
        return dictData.getLabel();
    }

    /**
     * 解析获得指定的字典数据，从缓存中
     *
     * @param type  字典类型
     * @param label 字典数据标签
     * @return 字典数据
     */
    DictDataDTO parseDictData(String type, String label);

    /**
     * 获得指定字典类型的字典数据列表
     *
     * @param dictTypes 字典类型
     * @return 字典数据列表
     */
    List<DictDataDTO> getDictDataList(List<String> dictTypes);

    /**
     * 获得字典数据标签列表
     *
     * @param dictType 字典类型
     * @return 字典数据标签列表
     */
    default List<String> getDictDataLabelList(String dictType) {
        List<DictDataDTO> list = getDictDataList(Lists.newArrayList(dictType));
        return convertList(list, DictDataDTO::getLabel);
    }

    /**
     * 获得字典数据列表
     *
     * @param fieldIds 字段ID
     * @return 字典数据列表
     */
    List<DictDataDTO> getFieldDictDataList(List<Long> fieldIds);

    /**
     * 批量插入字典数据
     *
     * @param dictDataList 字典数据列表
     */
    void insertBatch(List<DictDataDTO> dictDataList);

    /**
     * 批量更新字典数据
     *
     * @param dictDataList 字典数据列表
     */
    void updateBatch(List<DictDataDTO> dictDataList);

    /**
     * 批量删除字典数据
     *
     * @param dicDataIds 字典数据列表
     */
    void deleteBatchIds(Set<Long> dicDataIds);
}
