package com.fancy.module.agent.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fancy.component.mybatis.core.dataobject.BasePojo;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class AgUserBalanceDetail extends BasePojo {
    private static final long serialVersionUID = -65063725236600075L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 账户id
     */
    private Long agUserId;

    /**
     * 账户名称
     */
    private String name;
    /**
     * 账单类型 0出账 1入账
     * @see com.fancy.module.agent.enums.AgUserBalanceDetailBillType
     */
    private Integer billType;

    /**
     * 记录类型
     * @see com.fancy.module.agent.enums.AgUserBalanceDetailRecordType
     */
    private Integer recordType;

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
     * @see com.fancy.module.agent.enums.AgUserBalanceDetailType
     */
    private Integer objectType;

    /**
     * 关联的Id ContentFortTypeEnum = 订单id
     */
    private String objectId;

    /**
     * 关联内容服务类型
     * @see com.fancy.module.agent.enums.ContentFortTypeEnum
     */
    private Integer objectSubType;

    /**
     * 关联内容服务名称 回显字段
     */
    private String objectSubTypeName;

    /**
     * 关联内容服务明细名称 回显字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<AgMerchantOrder.AgMerchantOrderDetailVo> objectSubTypeDetailName;

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


}

