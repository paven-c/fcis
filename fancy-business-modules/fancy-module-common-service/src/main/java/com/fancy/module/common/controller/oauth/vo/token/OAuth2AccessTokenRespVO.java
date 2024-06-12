package com.fancy.module.common.controller.oauth.vo.token;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author paven
 */
@Schema(description = "访问令牌ResponseVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AccessTokenRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long id;

    @Schema(description = "访问令牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String accessToken;

    @Schema(description = "刷新令牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String refreshToken;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long userId;

    @Schema(description = "用户类型，参见 UserTypeEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Integer userType;

    @Schema(description = "客户端编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String clientId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expiresTime;

}
