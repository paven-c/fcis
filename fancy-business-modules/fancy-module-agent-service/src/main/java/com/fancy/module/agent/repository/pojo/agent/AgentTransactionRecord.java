package com.fancy.module.agent.repository.pojo.agent;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;


/**
 * @author paven
 */
@Data
@TableName("ag_agent_transaction_record")
public class AgentTransactionRecord implements Serializable {

    /**
     * 交易记录ID
     */
    private Long id;

    /**
     * 代理商ID
     */
    private Long agentId;

    /**
     * 代理商名称
     */
    private String agentName;

    /**
     * 交易类型
     */
    private Boolean transactionType;

    /**
     * 转账类型
     */
    private Boolean transferType;

    /**
     * 服务类型
     */
    private Integer serviceType;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 账户余额
     */
    private BigDecimal remainingAmount;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最近更新人
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除（0未删除 1已删除）
     */
    private Integer deleted;

}