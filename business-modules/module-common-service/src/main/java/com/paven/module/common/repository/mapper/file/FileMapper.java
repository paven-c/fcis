package com.paven.module.common.repository.mapper.file;


import com.paven.common.pojo.PageResult;
import com.paven.component.mybatis.core.mapper.BaseMapperX;
import com.paven.component.mybatis.core.query.LambdaQueryWrapperX;
import com.paven.module.common.controller.file.vo.file.FilePageReqVO;
import com.paven.module.common.repository.pojo.file.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件操作 Mapper
 *
 * @author paven
 */
@Mapper
public interface FileMapper extends BaseMapperX<File> {

    default PageResult<File> selectPage(FilePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<File>()
                .likeIfPresent(File::getPath, reqVO.getPath())
                .likeIfPresent(File::getType, reqVO.getType())
                .betweenIfPresent(File::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(File::getId));
    }

}
