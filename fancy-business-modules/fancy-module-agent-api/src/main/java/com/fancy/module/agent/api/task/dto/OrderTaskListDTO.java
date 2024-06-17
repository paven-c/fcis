package com.fancy.module.agent.api.task.dto;

import com.fancy.common.pojo.PageParam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xingchen
 * @version 1.0
 * @date 2024/6/11 11:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderTaskListDTO extends PageParam {

    /**
     * 客户id
     */
    private Long merchantId;

    /**
     * 内容名称
     */
    private String contentName;

    /**
     * 内容类型 1图片 2视频 3套餐
     */
    private Integer contentType;

    /**
     * 商品id
     */
    private List<Long> fancyItemIdList;

    /**
     * 商品名称
     */
    private String fancyItemName;

    /**
     * 任务状态 1生成中 2已完成 3已交付
     */
    private Integer taskStatus;

    /**
     * 任务创建时间
     */
    private LocalDateTime taskCreateTime;

    /**
     * 任务完成时间
     */
    private LocalDateTime taskFinishTime;

}
