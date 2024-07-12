package com.paven.component.excel.dict.config;

import com.paven.component.excel.dict.core.DictFrameworkUtils;
import com.paven.module.common.api.dict.DictDataApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author paven
 */
@AutoConfiguration
public class DictAutoConfiguration {

    @Bean
    public DictFrameworkUtils dictUtils(DictDataApi dictDataApi) {
        DictFrameworkUtils.init(dictDataApi);
        return new DictFrameworkUtils();
    }

}
