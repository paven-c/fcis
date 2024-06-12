package com.fancy.module.common.service.file;


import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.module.common.enums.ErrorCodeConstants.FILE_NOT_EXISTS;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.fancy.common.pojo.PageResult;
import com.fancy.common.util.io.FileUtils;
import com.fancy.common.util.object.BeanUtils;
import com.fancy.module.common.component.file.core.client.FileClient;
import com.fancy.module.common.component.file.core.client.s3.FilePresignedUrlRespDTO;
import com.fancy.module.common.component.file.core.utils.FileTypeUtils;
import com.fancy.module.common.controller.file.vo.file.FileCreateReqVO;
import com.fancy.module.common.controller.file.vo.file.FilePageReqVO;
import com.fancy.module.common.controller.file.vo.file.FilePresignedUrlRespVO;
import com.fancy.module.common.repository.mapper.file.FileMapper;
import com.fancy.module.common.repository.pojo.file.File;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * 文件 Service 实现类
 *
 * @author paven
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileMapper fileMapper;

    @Override
    public PageResult<File> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    @SneakyThrows
    public String createFile(String name, String path, byte[] content) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        File file = new File();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        fileMapper.insert(file);
        return url;
    }

    @Override
    public Long createFile(FileCreateReqVO createReqVO) {
        File file = BeanUtils.toBean(createReqVO, File.class);
        fileMapper.insert(file);
        return file.getId();
    }

    @Override
    public void deleteFile(Long id) throws Exception {
        // 校验存在
        File file = validateFileExists(id);

        // 从文件存储器中删除
        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.delete(file.getPath());

        // 删除记录
        fileMapper.deleteById(id);
    }

    private File validateFileExists(Long id) {
        File File = fileMapper.selectById(id);
        if (File == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return File;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }

    @Override
    public FilePresignedUrlRespVO getFilePresignedUrl(String path) throws Exception {
        FileClient fileClient = fileConfigService.getMasterFileClient();
        FilePresignedUrlRespDTO presignedObjectUrl = fileClient.getPresignedObjectUrl(path);
        return BeanUtils.toBean(presignedObjectUrl, FilePresignedUrlRespVO.class,
                object -> object.setConfigId(fileClient.getId()));
    }

}
