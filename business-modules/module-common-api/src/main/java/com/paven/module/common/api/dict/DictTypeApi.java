package com.paven.module.common.api.dict;


/**
 * 字典数据 API 接口
 *
 * @author paven
 */
public interface DictTypeApi {

    /**
     * 创建字典类型
     *
     * @param dictName 字典名称
     * @param dictType 字典类型
     * @return 字典类型编号
     */
    Long createDictType(String dictName, String dictType);
}
