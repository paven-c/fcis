package com.fancy.module.common.repository.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 代理商余额表(AgUserBalance)实体类
 *
 * @author makejava
 * @since 2024-06-11 09:26:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "ag_user_balance",autoResultMap = true)
public class AgUserBalance  implements Serializable {
    private static final long serialVersionUID = 295014010788188818L;
    /**
     * 代理商id
     */
    private Long agUserId; 
    
    /**
     * 代理商名称
     */
    private String name; 
    
    /**
     * 可用金额
     */
    private BigDecimal nowPrice; 
    
    /**
     * 变更前金额
     */
    private BigDecimal beforePrice; 
    
    /**
     * 0正常 1停用
     */
    private Integer status; 
    
    
    
    

}

