package com.fancy.common.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author paven
 */
@Data
public final class PageResult<T> implements Serializable {

    /**
     * 数据
     */
    private List<T> list;

    /**
     * 总量
     */
    private Integer total;

    private Integer pageNum;

    private Integer pageSize;

    public PageResult(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageResult(List<T> list, Integer total, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageResult(Integer total, Integer pageNum, Integer pageSize) {
        this.list = new ArrayList<>();
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static <T> PageResult<T> empty(Integer pageNum, Integer pageSize) {
        return new PageResult<>(pageNum, pageSize);
    }

    public static <T> PageResult<T> empty(Integer total, Integer pageNum, Integer pageSize) {
        return new PageResult<>(total, pageNum, pageSize);
    }

}
