package com.fancy.module.common.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 代理客户表(AgMerchant)实体类
 *
 * @author makejava
 * @since 2024-06-11 09:26:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "ag_merchant",autoResultMap = true)
public class AgMerchant implements Serializable {
    private static final long serialVersionUID = -61564167293543000L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * fc商户id
     */
    private Long merchantId; 
    
    /**
     * 创建人id
     */
    private Long creatorId; 
    
    /**
     * 部门id
     */
    private Long deptId; 
    
    /**
     * 代理商户名称
     */
    private String name; 
    
    /**
     * 商户头像
     */
    private String icon; 
    
    /**
     * 商户地址
     */
    private String addr; 
    
    /**
     * 客户图标
     */
    private String customerIcon; 
    
    /**
     * 身份证图片 正面,反面
     */
    private String idCard; 
    
    /**
     * 联系人手机号
     */
    private String contactsPhone; 
    
    /**
     * 联系方式人
     */
    private String contacts; 
    
    /**
     * 商户状态 0正常 1停用 2试用
     */
    private Integer merchantStatus; 
    
    /**
     * 商户类型, tb:淘宝, jd:京东, dy:抖音
     */
    private String merchantType; 
    
    /**
     * 店铺名称
     */
    private String shopName; 
    
    /**
     * 店铺id
     */
    private String shopId; 
    
    /**
     * 店铺地址
     */
    private String shopAddr; 
    
    /**
     * 发票抬头
     */
    private String invoiceHeader; 
    
    /**
     * 纳税人识别号
     */
    private String taxpayerIdentificationNumber; 
    
    /**
     * 授权状态 0 未授权，1 已授权
     */
    private Integer authStatus; 
    
    /**
     * 合同开始时间
     */
    private LocalDateTime contractStartTime; 
    
    /**
     * 合同结束时间
     */
    private LocalDateTime contractEndTime; 
    
    /**
     * 营业执照
     */
    private String businessLicense; 
    
    /**
     * 商户统一社会信用代码
     */
    private String code; 
    
    /**
     * 公司介绍
     */
    private String companyIntroduction;

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

