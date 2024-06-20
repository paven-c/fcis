package com.fancy.module.common.api.content.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 商户配置
 */
@Data
public class CmsMerchantReqDto implements Serializable {

    private static final long serialVersionUID = 2782568463233421565L;

    /**
     * 商户id
     */
    private Long merchantId;

    private String merchantName;

    private String shopName;
    /**
     * 商户头像
     */
    private String customerIcon;

    /**
     * 商户地址
     */
    private String addr;

    /**
     * 商户统一社会信用代码
     */
    private String code;

    /**
     * 商家联系方式
     */
    private String phone;

    /**
     * 商户平台
     */
    private String merchantType;

    /**
     * 店铺id
     */
    private String storeId;
}
