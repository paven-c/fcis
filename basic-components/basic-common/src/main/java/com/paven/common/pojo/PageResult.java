package com.paven.common.pojo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author paven
 */
@Data
public class PageResult<T> implements IPage<T> {

    /**
     * 数据
     */
    private List<T> list;

    /**
     * 总数
     */
    private Integer totalCount;

    /**
     * 总页数
     */
    private Integer pageCount;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 分页条数
     */
    private Integer pageSize;

    public PageResult(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = 0;
    }

    public PageResult(List<T> list, Integer totalCount, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageResult(Integer totalCount, Integer pageNum, Integer pageSize) {
        this.list = new ArrayList<>();
        this.totalCount = totalCount;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static <T> PageResult<T> build(PageParam pageParam) {
        return new PageResult<>(pageParam.getPageNum(), pageParam.getPageSize());
    }

    public static <T> PageResult<T> empty(Integer pageNum, Integer pageSize) {
        return new PageResult<>(pageNum, pageSize);
    }

    public static <T> PageResult<T> empty(Integer total, Integer pageNum, Integer pageSize) {
        return new PageResult<>(total, pageNum, pageSize);
    }

    public static <T, K> PageResult<T> convert(List<T> list, PageResult<K> pageResult) {
        return new PageResult<>(list, pageResult.getTotalCount(), pageResult.getPageNum(), pageResult.getPageSize());
    }

    public Integer getPageCount() {
        return (totalCount + pageSize - 1) / pageSize;
    }

    @Override
    @JsonIgnore
    public List<OrderItem> orders() {
        return Lists.newArrayList();
    }

    @Override
    @JsonIgnore
    public List<T> getRecords() {
        return list;
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.list = records;
        return this;
    }

    @Override
    @JsonIgnore
    public long getTotal() {
        return totalCount;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.totalCount = (int) total;
        return this;
    }

    @Override
    @JsonIgnore
    public long getSize() {
        return pageSize;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.pageSize = (int) size;
        return this;
    }

    @Override
    @JsonIgnore
    public long getCurrent() {
        return pageNum;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.pageNum = (int) current;
        return this;
    }
}
