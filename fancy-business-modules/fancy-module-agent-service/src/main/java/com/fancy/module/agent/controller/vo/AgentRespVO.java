package com.fancy.module.agent.controller.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelProperty("代理商编号")
    private Long id;

    /**
     * 代理商名称
     */
    @ExcelProperty("代理商名称")
    private String agentName;

    /**
     * 代理省份
     */
    @ExcelProperty("代理区域-省")
    private Long provinceId;

    /**
     * 代理城市
     */
    @ExcelProperty("代理区域-城市")
    private Long cityId;

    /**
     * 代理商等级
     */
    @ExcelProperty("代理商等级")
    private Integer level;

    /**
     * 联系人姓名
     */
    @ExcelProperty("联系人")
    private String contactorName;

    /**
     * 手机号码
     */
    @ExcelProperty("手机号码")
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 部门编号
     */
    private Long deptId;

    /**
     * 上级代理ID
     */
    private Long parentAgentId;

    /**
     * 身份证正面图片链接
     */
    @ExcelProperty("身份证正面图片链接")
    private String idCardFrontSide;

    /**
     * 身份证反面图片链接
     */
    @ExcelProperty("身份证反面图片链接")
    private String idCardBackSide;

    /**
     * 营业执照图片链接
     */
    @ExcelProperty("营业执照图片链接")
    private String businessLicense;

    /**
     * 代理合同图片链接
     */
    @ExcelProperty("代理合同图片链接")
    private String contractLink;

    /**
     * 公司介绍
     */
    @ExcelProperty("公司介绍")
    private String introduction;

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

}
