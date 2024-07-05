package com.paven.module.common.convert.file;

import com.paven.module.common.controller.file.vo.config.FileConfigSaveReqVO;
import com.paven.module.common.repository.pojo.file.FileConfig;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T16:40:02+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class FileConfigConvertImpl implements FileConfigConvert {

    @Override
    public FileConfig convert(FileConfigSaveReqVO bean) {
        if ( bean == null ) {
            return null;
        }

        FileConfig.FileConfigBuilder fileConfig = FileConfig.builder();

        fileConfig.id( bean.getId() );
        fileConfig.name( bean.getName() );
        fileConfig.storage( bean.getStorage() );
        fileConfig.remark( bean.getRemark() );

        return fileConfig.build();
    }
}
