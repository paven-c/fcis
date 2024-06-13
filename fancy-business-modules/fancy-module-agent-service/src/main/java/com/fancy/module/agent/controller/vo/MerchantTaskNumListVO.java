package com.fancy.module.agent.controller.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 订单任务vo
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Data
public class MerchantTaskNumListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    private Integer serviceTotalNum;

    /**
     * 已完成
     */
    private Integer serviceConsumeNum;

    /**
     * 内容id
     */
    private Long contentServiceId;

    /**
     * 内容名称
     */
    private String contentServiceName;

    /**
     * 跳转平台 0不跳转 1内容平台
     */
    private Integer toPlatform;

}
