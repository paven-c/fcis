package com.paven.module.compliance.controller.form.vo;

import com.paven.module.compliance.controller.field.vo.FieldRespVO;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
@Builder
public class FormRespVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 建筑属性ID
     */
    private Long id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 状态（0停用 1启用）
     */
    private Integer status;

    /**
     * 字段条件
     */
    private String conditions;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者，目前使用 SysUser 的 id 编号
     */
    private String creator;

    /**
     * 更新者，目前使用 SysUser 的 id 编号
     */
    private String updater;

    /**
     * 字段列表
     */
    private List<FieldRespVO> fieldList;

}