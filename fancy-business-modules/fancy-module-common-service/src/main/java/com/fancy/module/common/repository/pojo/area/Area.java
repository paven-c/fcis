package com.fancy.module.common.repository.pojo.area;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;


/**
 * @author Yanyi
 */
@Data
@TableName("area")
public class Area implements Serializable {

    /**
     * 区域ID
     */
    private Long id;

    /**
     * 父区域ID
     */
    private Long parentId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 逻辑删除标记 0未删除、1已删除
     */
    private Boolean deleted;

}