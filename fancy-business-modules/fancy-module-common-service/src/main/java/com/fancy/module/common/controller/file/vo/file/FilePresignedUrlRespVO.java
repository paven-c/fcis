package com.fancy.module.common.controller.file.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "文件预签名地址ResponseVO")
@Data
public class FilePresignedUrlRespVO {

    @Schema(description = "配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11")
    private Long configId;

    @Schema(description = "文件上传 URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String uploadUrl;

    @Schema(description = "文件访问 URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String url;

}
