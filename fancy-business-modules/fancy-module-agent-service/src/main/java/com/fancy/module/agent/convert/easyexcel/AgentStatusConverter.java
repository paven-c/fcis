package com.fancy.module.agent.convert.easyexcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.fancy.module.agent.enums.AgentStatusEnum;

/**
 * @author paven
 */
public class AgentStatusConverter implements Converter<Integer> {

    @Override
    public Class<Integer> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration)
            throws Exception {
        AgentStatusEnum statusEnum = AgentStatusEnum.findByStatus(value);
        if (statusEnum != null) {
            return new WriteCellData<>(statusEnum.getDesc());
        } else {
            return new WriteCellData<>("未知状态");
        }
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }
}