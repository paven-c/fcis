package com.fancy.module.common.controller.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Schema(description = "用户导入ResponseVO")
@Data
@Builder
public class UserImportRespVO {

    @Schema(description = "创建成功的用户名数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> createUsernames;

    @Schema(description = "更新成功的用户名数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> updateUsernames;

    @Schema(description = "导入失败的用户集合，key 为用户名，value 为失败原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, String> failureUsernames;

}
