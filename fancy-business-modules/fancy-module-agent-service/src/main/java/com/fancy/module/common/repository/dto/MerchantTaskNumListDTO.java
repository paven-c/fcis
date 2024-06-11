package com.fancy.module.common.repository.dto;

import com.fancy.common.pojo.PageParam;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xingchen
 * @version 1.0
 * @date 2024/6/11 11:24
 */
@Data
public class MerchantTaskNumListDTO {

    /**
     * 客户id
     */
    private Long merchantId;

}
