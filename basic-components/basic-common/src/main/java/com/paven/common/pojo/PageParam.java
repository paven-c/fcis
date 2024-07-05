package com.paven.common.pojo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;

/**
 * @author paven
 */
@Data
public class PageParam implements Serializable {

    private static final Integer PAGE_NO = 1;

    private static final Integer PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE_NONE = -1;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNum = PAGE_NO;

    @NotNull(message = "分页条数不能为空")
    @Min(value = 1, message = "分页条数最小值为 1")
    @Max(value = 100, message = "分页条数最大值为 100")
    private Integer pageSize = PAGE_SIZE;

}
