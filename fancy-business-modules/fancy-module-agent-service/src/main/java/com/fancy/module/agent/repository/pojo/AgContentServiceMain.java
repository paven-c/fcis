package com.fancy.module.agent.repository.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容服务主表
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgContentServiceMain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 修改人id
     */
    private Long updateId;

    /**
     * 修改人
     */
    private String updateName;


}
