package com.fancy.module.agent.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fancy.component.mybatis.core.dataobject.BasePojo;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单任务表
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgOrderTask extends BasePojo implements Serializable {

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
     * 部门id
     */
    private Long deptId;


}
