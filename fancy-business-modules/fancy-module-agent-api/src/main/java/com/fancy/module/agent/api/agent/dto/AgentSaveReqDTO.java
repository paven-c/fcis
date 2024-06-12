package com.fancy.module.agent.api.agent.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author paven
 */
@Data
public class AgentSaveReqDTO {

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
     * 部门ID
     */
    private Long deptId;

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
     * 合同图片链接
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

}
