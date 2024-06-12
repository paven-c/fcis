package com.fancy.module.common.convert.file;

import com.fancy.module.common.controller.file.vo.config.FileConfigSaveReqVO;
import com.fancy.module.common.repository.pojo.file.FileConfig;
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
