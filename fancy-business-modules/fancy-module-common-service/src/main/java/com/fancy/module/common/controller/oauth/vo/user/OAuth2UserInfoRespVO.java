package com.fancy.module.common.controller.oauth.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "OAuth2 获得用户基本信息ResponseVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserInfoRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long id;

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String username;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String nickname;

    @Schema(description = "用户邮箱", example = "")
    private String email;

    @Schema(description = "手机号码", example = "")
    private String mobile;

    @Schema(description = "用户性别，参见 GenderEnum 枚举类", example = "")
    private Integer gender;

    @Schema(description = "用户头像", example = "")
    private String avatar;

    /**
     * 所在部门
     */
    private Dept dept;

    @Schema(description = "部门")
    @Data
    public static class Dept {

        @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private Long id;

        @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String name;

    }
}
