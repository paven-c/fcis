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
 * 内容服务子表
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgContentServiceDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据从属的主表id
     */
    private Long mainId;

    /**
     * 所选内容的主表id
     */
    private Long contentId;

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
