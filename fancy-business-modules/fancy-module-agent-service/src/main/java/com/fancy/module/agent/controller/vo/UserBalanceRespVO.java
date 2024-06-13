package com.fancy.module.agent.controller.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author paven
 */
@Data
@Accessors(chain = true)
public class UserBalanceRespVO implements Serializable {

    private Long id;
    
    /**
     * 用户名称
     */
    private String name;

    /**
     * 账户余额
     */
    private BigDecimal nowPrice; 

    /**
     * 0停用 1启用
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

}

