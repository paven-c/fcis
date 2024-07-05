package com.paven.module.common.repository.mapper.file;

import com.paven.common.pojo.PageResult;
import com.paven.component.mybatis.core.mapper.BaseMapperX;
import com.paven.component.mybatis.core.query.LambdaQueryWrapperX;
import com.paven.module.common.controller.file.vo.config.FileConfigPageReqVO;
import com.paven.module.common.repository.pojo.file.FileConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileConfigMapper extends BaseMapperX<FileConfig> {

    default PageResult<FileConfig> selectPage(FileConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FileConfig>()
                .likeIfPresent(FileConfig::getName, reqVO.getName())
                .eqIfPresent(FileConfig::getStorage, reqVO.getStorage())
                .betweenIfPresent(FileConfig::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FileConfig::getId));
    }

    default FileConfig selectByMaster() {
        return selectOne(FileConfig::getMaster, true);
    }

}
