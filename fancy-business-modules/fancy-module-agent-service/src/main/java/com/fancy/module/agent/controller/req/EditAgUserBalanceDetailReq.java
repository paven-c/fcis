package com.fancy.module.agent.controller.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fancy.module.agent.enums.AgUserBalanceDetailType;
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
public class EditAgUserBalanceDetailReq implements Serializable {
    private static final long serialVersionUID = -65033725236600075L;

    /**
     * 代理商id
     */
    private Long toAgUserId;
    /**
     * 来源用户Id
     */
    private Long fromAgUserId;

    /**
     * 代理商名称
     */
    private String toAgUsername;
    /**
     * 来源用户名称
     */
    private String fromUserName;

    /**
     * 是否检查 来源账号余额
     */
    private Boolean checkFrom = false;
    
    /**
     * 变更金额
     */
    private BigDecimal price; 
    
    /**
     * 变更类型
     */
    private AgUserBalanceDetailType objectType;
    
    /**
     * 关联的Id
     */
    private String object; 

    
    /**
     * 备注
     */
    private String remarks;


}

