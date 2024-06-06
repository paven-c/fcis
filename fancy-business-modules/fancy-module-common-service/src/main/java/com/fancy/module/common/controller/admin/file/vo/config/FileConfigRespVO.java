package com.fancy.module.common.controller.admin.file.vo.config;

import com.fancy.module.common.component.file.core.client.FileClientConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Schema(description = "文件配置ResponseVO")
@Data
public class FileConfigRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "配置名", requiredMode = Schema.RequiredMode.REQUIRED, example = "S3 - 阿里云")
    private String name;

    @Schema(description = "存储器，参见 FileStorageEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer storage;

    @Schema(description = "是否为主配置", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean master;

    @Schema(description = "存储配置", requiredMode = Schema.RequiredMode.REQUIRED)
    private FileClientConfig config;

    @Schema(description = "备注", example = "我是备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
