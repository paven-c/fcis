package com.fancy.module.common.repository.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 订单任务vo
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Data
public class OrderTaskListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 客户id
     */
    private Long merchantId;

    /**
     * 导入任务id
     */
    private Long taskId;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 商品id
     */
    private Long fancyItemId;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除0启用1删除
     */
    private Integer deleted;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 内容类型 1图片 2视频 3套餐
     */
    private Integer contentType;

    /**
     * 内容名称
     */
    private String contentName;



}
