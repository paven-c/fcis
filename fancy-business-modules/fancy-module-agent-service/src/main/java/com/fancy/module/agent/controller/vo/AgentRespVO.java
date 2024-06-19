package com.fancy.module.agent.controller.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fancy.module.agent.convert.easyexcel.AgentLevelConverter;
import com.fancy.module.agent.convert.easyexcel.AgentStatusConverter;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author paven
 */
@Data
@ExcelIgnoreUnannotated
public class AgentRespVO {

    /**
     * 代理商ID
     */
    @ExcelProperty("代理商ID")
    private Long id;

    /**
     * 代理商名称
     */
    @ExcelProperty("代理商名称")
    private String agentName;

    /**
     * 代理商等级
     */
    @ExcelProperty(value = "代理商等级", converter = AgentLevelConverter.class)
    private Integer level;

    /**
     * 上级代理名称
     */
    @ExcelProperty("上级代理商")
    private String parentAgentName;

    /**
     * 联系人姓名
     */
    @ExcelProperty("联系人")
    private String contactorName;

    /**
     * 手机号码
     */
    @ExcelProperty("手机号")
    private String mobile;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 审核通过时间
     */
    @ExcelProperty("审核通过时间")
    private LocalDateTime approveTime;

    /**
     * 合作开始时间
     */
    @ExcelProperty("合作开始时间")
    private LocalDateTime beginTime;

    /**
     * 合作结束时间
     */
    @ExcelProperty("合作结束时间")
    private LocalDateTime endTime;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态",converter = AgentStatusConverter.class)
    private Integer status;

    /**
     * 当前余额
     */
    private String currentBalance;

    /**
     * 代理省份
     */
    private Long provinceId;

    /**
     * 代理城市
     */
    private Long cityId;

    /**
     * 上级代理ID
     */
    private Long parentAgentId;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 部门编号
     */
    private Long deptId;

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
     * 代理合同名称
     */
    private String contractName;

    /**
     * 公司介绍
     */
    private String introduction;

}
