package com.fancy.module.common.controller.oauth.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Schema(description = "OAuth2 客户端ResponseVO")
@Data
public class OAuth2ClientRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long id;

    @Schema(description = "客户端编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String clientId;

    @Schema(description = "客户端密钥", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String secret;

    @Schema(description = "应用名", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;

    @Schema(description = "应用图标", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String logo;

    @Schema(description = "应用描述", example = "")
    private String description;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer status;

    @Schema(description = "访问令牌的有效期", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer accessTokenValiditySeconds;

    @Schema(description = "刷新令牌的有效期", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer refreshTokenValiditySeconds;

    @Schema(description = "可重定向的 URI 地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private List<String> redirectUris;

    @Schema(description = "授权类型，参见 OAuth2GrantTypeEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private List<String> authorizedGrantTypes;

    @Schema(description = "授权范围", example = "")
    private List<String> scopes;

    @Schema(description = "自动通过的授权范围", example = "")
    private List<String> autoApproveScopes;

    @Schema(description = "权限", example = "common:user:query")
    private List<String> authorities;

    @Schema(description = "资源", example = "")
    private List<String> resourceIds;

    @Schema(description = "附加信息", example = "{}")
    private String additionalInformation;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
