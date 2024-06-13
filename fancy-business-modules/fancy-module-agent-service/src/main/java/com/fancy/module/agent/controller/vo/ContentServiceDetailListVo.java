package com.fancy.module.agent.controller.vo;

import java.util.List;
import lombok.Data;

/**
 * @author xingchen
 * @version 1.0
 * @date 2024/6/13 17:56
 */

@Data
public class ContentServiceDetailListVo {
    /**
     * 主键id
     */
    private Long detailId;

    /**
     * 内容类型 1图片 2视频 3套餐
     */
    private Integer detailContentType;

    /**
     * 内容名称
     */
    private String detailContentName;

    /**
     * 覆盖范围类型 1覆盖全店 2部分覆盖
     */
    private Integer coverageArea;

    /**
     * 覆盖sku数量
     */
    private Integer coverageSkuNum;

    /**
     * 覆盖条数
     */
    private Integer coverageNum;

}
