package com.fancy.module.agent.controller.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ContentServiceMainListVo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 内容类型 1图片 2视频 3套餐
     */
    private Integer contentType;

    /**
     * 内容名称
     */
    private String contentName;

    /**
     * 内容状态 0未生效 1生效 2失效
     */
    private Integer contentStatus;

    /**
     * 消耗点数
     */
    private Integer consumePoint;

    /**
     * 内容服务描述
     */
    private String contentDesc;

    /**
     * 内容服务素材要求
     */
    private String contentAssetsDesc;

    /**
     * 内容展示，多个逗号分隔
     */
    private String contentShowUrl;

    /**
     * 内容服务介绍
     */
    private String contentDescUrl;

    /**
     * 详情
     */
    private List<ContentServiceDetailListVo> contentServiceDetailListVo;

}
