package com.fancy.module.agent.controller.vo;

import com.fancy.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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
public class AgUploadTaskReqVO {

    private Integer successCount;

    /**
     * 错误信息
     */
    private List<AgUploadTaskErrorVo> agUploadTaskErrorVoList;

    @Data
    public static class AgUploadTaskErrorVo {

        private Long contentId;

        private Long fancyItemId;

        private String errorMsg;

    }

}
