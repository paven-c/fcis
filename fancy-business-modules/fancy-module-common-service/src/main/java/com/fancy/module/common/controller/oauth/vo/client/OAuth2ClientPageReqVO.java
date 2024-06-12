package com.fancy.module.common.controller.oauth.vo.client;

import com.fancy.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author paven
 */
@Schema(description = "OAuth2 客户端分页RequestVO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OAuth2ClientPageReqVO extends PageParam {

    @Schema(description = "应用名，模糊匹配", example = "")
    private String name;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举", example = "")
    private Integer status;

}
