package com.fancy.module.agent.controller.req;

import com.baomidou.mybatisplus.annotation.TableName;
import com.opencsv.bean.CsvBindByName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author Yanyi
 */
@Data
public class AgOrderTaskImportReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID,没有新增，有覆盖导入
     */
    @CsvBindByName(column = "任务ID（覆盖导入写）")
    private Long id;

    /**
     * 任务状态
     */
    @CsvBindByName(column = "任务状态")
    private Integer taskStatus;

    /**
     * 客户ID
     */
    @CsvBindByName(column = "客户ID")
    private Long agMerchantId;

    /**
     * 内容服务ID
     */
    @CsvBindByName(column = "内容服务ID")
    private Long contentId;

    /**
     * 商品ID
     */
    @CsvBindByName(column = "商品ID")
    private Long fancyItemId;

    /**
     * 商品名称
     */
    @CsvBindByName(column = "商品名称")
    private String fancyItemName;

    /**
     * 任务创建时间
     */
    @CsvBindByName(column = "任务创建时间")
    private LocalDateTime taskCreateTime;

    /**
     * 任务完成时间
     */
    @CsvBindByName(column = "任务完成时间")
    private LocalDateTime taskFinishTime;

    /**
     * 导入人ID
     */
    @CsvBindByName(column = "导入人ID")
    private Long createId;
}