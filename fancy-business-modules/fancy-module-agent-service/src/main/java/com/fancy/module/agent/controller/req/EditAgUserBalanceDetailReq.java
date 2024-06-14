package com.fancy.module.agent.controller.req;

import com.alibaba.fastjson.JSONObject;
import com.fancy.module.agent.enums.AgUserBalanceDetailType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
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
public class EditAgUserBalanceDetailReq implements Serializable {
    private static final long serialVersionUID = -65033725236600075L;

    /**
     * 代理商id
     */
    private Long toAgUserId;

    /**
     * 代理商名称
     */
    private String toAgUsername;

    /**
     * 来源用户Id
     */
    private Long fromAgUserId;

    /**
     * 来源用户名称
     */
    private String fromUserName;

    /**
     * 是否检查 来源账号余额
     */
    private Boolean checkFrom = true;
    private Boolean checkTo = true;

    /**
     * 变更金额
     */
    private BigDecimal price; 
    
    /**
     * 变更类型
     */
    private AgUserBalanceDetailType objectType;

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
     * 备注
     */
    private String remarks;


    
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
    private List<AgMerchantOrder.AgMerchantOrderDetailVo> objectSubTypeDetailName;




}

