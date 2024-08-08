package com.paven.component.excel.dict.core;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.paven.common.core.KeyValue;
import com.paven.common.util.cache.CacheUtils;
import com.paven.module.common.api.dict.DictDataApi;
import com.paven.module.common.api.dict.dto.DictDataDTO;
import java.time.Duration;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 字典工具类
 *
 * @author paven
 */
@Slf4j
public class DictFrameworkUtils {

    private static DictDataApi dictDataApi;

    private static final DictDataDTO DICT_DATA_NULL = new DictDataDTO();

    /**
     * 针对 {@link #getDictDataLabel(String, String)} 的缓存
     */
    private static final LoadingCache<KeyValue<String, String>, DictDataDTO> GET_DICT_DATA_CACHE = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), new CacheLoader<KeyValue<String, String>, DictDataDTO>() {
                @Override
                public DictDataDTO load(KeyValue<String, String> key) {
                    return ObjectUtil.defaultIfNull(dictDataApi.getDictData(key.getKey(), key.getValue()), DICT_DATA_NULL);
                }
            });

    /**
     * 针对 {@link #getDictDataLabelList(String)} 的缓存
     */
    private static final LoadingCache<String, List<String>> GET_DICT_DATA_LIST_CACHE = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), new CacheLoader<String, List<String>>() {
                @Override
                public List<String> load(String dictType) {
                    return dictDataApi.getDictDataLabelList(dictType);
                }
            });

    /**
     * 针对 {@link #parseDictDataValue(String, String)} 的缓存
     */
    private static final LoadingCache<KeyValue<String, String>, DictDataDTO> PARSE_DICT_DATA_CACHE = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), new CacheLoader<KeyValue<String, String>, DictDataDTO>() {
                @Override
                public DictDataDTO load(KeyValue<String, String> key) {
                    return ObjectUtil.defaultIfNull(dictDataApi.parseDictData(key.getKey(), key.getValue()), DICT_DATA_NULL);
                }
            });

    public static void init(DictDataApi dictDataApi) {
        DictFrameworkUtils.dictDataApi = dictDataApi;
        log.info("[init][初始化 DictFrameworkUtils 成功]");
    }

    @SneakyThrows
    public static String getDictDataLabel(String dictType, Integer value) {
        return GET_DICT_DATA_CACHE.get(new KeyValue<>(dictType, String.valueOf(value))).getLabel();
    }

    @SneakyThrows
    public static String getDictDataLabel(String dictType, String value) {
        return GET_DICT_DATA_CACHE.get(new KeyValue<>(dictType, value)).getLabel();
    }

    @SneakyThrows
    public static List<String> getDictDataLabelList(String dictType) {
        return GET_DICT_DATA_LIST_CACHE.get(dictType);
    }

    @SneakyThrows
    public static String parseDictDataValue(String dictType, String label) {
        return PARSE_DICT_DATA_CACHE.get(new KeyValue<>(dictType, label)).getValue();
    }

}
