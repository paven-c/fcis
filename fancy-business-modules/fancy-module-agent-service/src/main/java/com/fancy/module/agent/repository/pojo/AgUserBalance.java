package com.fancy.module.agent.repository.pojo;

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

