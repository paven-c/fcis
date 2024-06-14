package com.fancy.module.agent.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 代理商余额明细表(AgUserBalanceDetail)实体类
 *
 * @author makejava
 * @since 2024-06-11 09:26:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "ag_user_balance_detail",autoResultMap = true)
public class AgUserBalanceDetail implements Serializable {
    private static final long serialVersionUID = -65063725236600075L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 代理商id
     */
    private Long agUserId;

    /**
     * 代理商名称
     */
    private String name;
    /**
     * 账单类型 0出账 1入账
     * @see com.fancy.module.agent.enums.AgUserBalanceDetailBillType
     */
    private Integer billType;

    /**
     * 账单类型用户Id
     */
    private Long billTypeAgUserId;
    /**
     * 账单类型用户名称
     */
    private String billTypeAgUserName;


    /**
     * 变更前金额
     */
    private BigDecimal beforePrice; 
    
    /**
     * 变更后金额
     */
    private BigDecimal afterPrice; 
    
    /**
     * 变更金额
     */
    private BigDecimal price;


    
    /**
     * 变更类型
     */
    private Integer objectType;

    /**
     * 关联的Id
     */
    private String object;

    /**
     * 关联服务类型
     */
    private Integer objectSubType;

    /**
     * 关联服务名称
     */
    private String objectSubName;

    /**
     * 操作人Id
     */
    private Long  createId;

    /**
     * 操作人名称
     */
    private String createName;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 状态 0正常 1停用 2完成
     */
    private Integer status = 0;
    
    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否删除0启用1删除
     */
    private Integer deleted;
    
    

}

