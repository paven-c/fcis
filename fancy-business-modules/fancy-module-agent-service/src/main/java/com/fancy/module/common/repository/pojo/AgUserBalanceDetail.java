package com.fancy.module.common.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 状态 0正常 1停用 2完成
     */
    private Integer status; 
    
    /**
     * 备注
     */
    private String remarks;

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

