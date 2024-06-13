package com.fancy.module.agent.repository.pojo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 代理客户订单明显表(AgMerchantOrder)实体类
 *
 * @author makejava
 * @since 2024-06-11 09:26:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "ag_merchant_order_detail",autoResultMap = true)
public class AgMerchantOrderDetail implements Serializable {
    private static final long serialVersionUID = 952798869645981870L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单Id
     */
    private Long agMerchantOrderId;
    /**
     * 代理商户id
     */
    private Long agMerchantId; 
    
    /**
     * 代理商户名称
     */
    private String name; 
    
    /**
     * fc商户id
     */
    private Long merchantId; 
    
    /**
     * 创建人Id
     */
    private Long creatorId; 
    
    /**
     * 部门id
     */
    private Long deptId; 
    
    /**
     * 订单名称
     */
    private String orderName; 
    
    /**
     * 订单金额
     */
    private BigDecimal orderMoney; 
    
    /**
     * 单价金额
     */
    private BigDecimal orderUnitPrice;

    /**
     * 服务版本id
     */
    private Long contentServiceId;

    /**
     * 服务版本类型
     */
    private Integer serviceType;


    /**
     * 订单状态 0正常 1停用 2完成
     */
    private Integer serviceStatus;
    
    /**
     * 服务任务总数
     */
    private Long serviceTotalNum; 
    
    /**
     * 服务消耗任务数
     */
    private Long serviceConsumeNum; 
    
    /**
     * 合同开始时间
     */
    private LocalDateTime contractStartTime; 
    
    /**
     * 合同结束时间
     */
    private LocalDateTime contractEndTime;

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
    
    

}

