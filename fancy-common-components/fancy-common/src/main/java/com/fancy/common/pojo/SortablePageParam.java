package com.fancy.common.pojo;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 可排序的分页参数
 *
 * @author paven
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SortablePageParam extends PageParam {

    /**
     * 排序字段
     */
    private List<SortingField> sortingFields;

}