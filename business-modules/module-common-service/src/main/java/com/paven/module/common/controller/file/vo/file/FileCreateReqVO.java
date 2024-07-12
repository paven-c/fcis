package com.paven.module.common.controller.file.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author paven
 */
@Schema(description = "文件创建RequestVO")
@Data
public class FileCreateReqVO {

    @NotNull(message = "文件配置编号不能为空")
    @Schema(description = "文件配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11")
    private Long configId;

    @NotNull(message = "文件路径不能为空")
    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED, example = ".jpg")
    private String path;

    @NotNull(message = "原文件名不能为空")
    @Schema(description = "原文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = ".jpg")
    private String name;

    @NotNull(message = "文件 URL不能为空")
    @Schema(description = "文件 URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String url;

    @Schema(description = "文件 MIME 类型", example = "application/octet-stream")
    private String type;

    @Schema(description = "文件大小", example = "2048", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer size;

}
