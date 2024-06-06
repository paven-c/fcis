package com.fancy.module.common.repository.mapper.file;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fancy.module.common.repository.pojo.file.FileContent;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileContentMapper extends BaseMapper<FileContent> {

    default void deleteByConfigIdAndPath(Long configId, String path) {
        this.delete(new LambdaQueryWrapper<FileContent>()
                .eq(FileContent::getConfigId, configId)
                .eq(FileContent::getPath, path));
    }

    default List<FileContent> selectListByConfigIdAndPath(Long configId, String path) {
        return selectList(new LambdaQueryWrapper<FileContent>()
                .eq(FileContent::getConfigId, configId)
                .eq(FileContent::getPath, path));
    }

}
