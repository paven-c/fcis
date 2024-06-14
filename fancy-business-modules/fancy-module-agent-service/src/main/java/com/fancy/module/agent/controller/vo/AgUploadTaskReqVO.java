package com.fancy.module.agent.controller.vo;

import com.fancy.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author paven
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgUploadTaskReqVO extends PageParam {

    private Long contentId;

    private Long fancyItemId;

    private String errorMsg;

}
