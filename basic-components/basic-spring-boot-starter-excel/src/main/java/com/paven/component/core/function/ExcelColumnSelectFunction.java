package com.paven.component.core.function;

import java.util.List;

/**
 * Excel 列下拉数据源获取接口
 */
public interface ExcelColumnSelectFunction {

    /**
     * 获得方法名称
     *
     * @return 方法名称
     */
    String getName();

    /**
     * 获得列下拉数据源
     *
     * @return 下拉数据源
     */
    List<String> getOptions();

}
