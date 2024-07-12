package com.paven.module.common.convert.file;

import com.paven.module.common.controller.file.vo.config.FileConfigSaveReqVO;
import com.paven.module.common.repository.pojo.file.FileConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 文件配置 Convert
 *
 * @author paven
 */
@Mapper
public interface FileConfigConvert {

    FileConfigConvert INSTANCE = Mappers.getMapper(FileConfigConvert.class);

    @Mapping(target = "config", ignore = true)
    FileConfig convert(FileConfigSaveReqVO bean);

}
