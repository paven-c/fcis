package com.fancy.module.agent.controller.req;

import com.fancy.common.pojo.PageParam;
import com.fancy.module.agent.enums.AgUserBalanceDetailType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 代理商余额明细表(AgUserBalanceDetail)实体类
 *
 * @author makejava
 * @since 2024-06-11 09:26:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryAgUserBalanceDetailReq extends PageParam{

    /**
     * 代理商id
     */
    private Long agUserId;

    /**
     * 代理商名称
     */
    private String agUsername;

    /**
     * 变更类型
     */
    private Integer billType;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建人id
     */
    private List<Long> creatorIds;

    /**
     * 部门id
     */
    private List<Long>  deptIds;



}

