package com.fancy.module.agent.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单扣减流水表
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgOrderFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户id
     */
    private Long agMerchantId;

    /**
     * 导入任务id
     */
    private Long taskId;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 扣减任务数
     */
    private Integer deductPoint;

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


}
