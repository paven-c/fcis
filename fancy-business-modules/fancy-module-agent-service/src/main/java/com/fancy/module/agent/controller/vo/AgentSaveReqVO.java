package com.fancy.module.agent.controller.vo;

import com.fancy.module.agent.enums.AgentStatusEnum;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * @author paven
 */
@Data
@Builder
public class AgentSaveReqVO {

    /**
     * 代理商ID
     */
    private Long id;

    /**
     * 代理商名称
     */
    private String agentName;

    /**
     * 代理省份
     */
    private Long provinceId;

    /**
     * 代理城市
     */
    private Long cityId;

    /**
     * 代理商等级
     */
    private Integer level;

    /**
     * 联系人姓名
     */
    private String contactorName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 上级代理ID
     */
    private Long parentAgentId;

    /**
     * 身份证正面图片链接
     */
    private String idCardFrontSide;

    /**
     * 身份证反面图片链接
     */
    private String idCardBackSide;

    /**
     * 营业执照图片链接
     */
    private String businessLicense;

    /**
     * 代理合同图片链接
     */
    private String contractLink;

    /**
     * 公司介绍
     */
    private String introduction;

    /**
     * 合作开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 合作结束时间
     */
    private LocalDateTime endTime;

    /**
     * 审核通过时间
     */
    private LocalDateTime approveTime;

    /**
     * 状态
     * <p>
     * {@link AgentStatusEnum}
     */
    private Integer status;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 部门编号
     */
    private Long deptId;

}
