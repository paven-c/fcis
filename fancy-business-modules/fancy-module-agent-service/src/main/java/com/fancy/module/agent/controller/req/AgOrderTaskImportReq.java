package com.fancy.module.agent.controller.req;

import com.alibaba.excel.annotation.ExcelProperty;
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
     * 主键ID,没有 新增，有 覆盖导入
     */
    @ExcelProperty("任务ID（覆盖导入需要）")
    private Long id;

    /**
     * 任务状态
     */
    @ExcelProperty("任务状态")
    private String taskStatus;

    /**
     * 客户ID
     */
    @ExcelProperty("客户ID")
    private Long agMerchantId;

    /**
     * 内容服务ID
     */
    @ExcelProperty("内容服务ID")
    private Long contentId;

    /**
     * 商品ID
     */
    @ExcelProperty("商品ID")
    private Long fancyItemId;

    /**
     * 商品名称
     */
    @ExcelProperty("商品名称")
    private String fancyItemName;

    /**
     * 任务创建时间
     */
    @ExcelProperty("任务创建时间")
    private LocalDateTime taskCreateTime;

    /**
     * 任务完成时间
     */
    @ExcelProperty("任务完成时间")
    private LocalDateTime taskFinishTime;

    /**
     * 导入人ID
     */
    @ExcelProperty("导入人ID")
    private Long createId;
}