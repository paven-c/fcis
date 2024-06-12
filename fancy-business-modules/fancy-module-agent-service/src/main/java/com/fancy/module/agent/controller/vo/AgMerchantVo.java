package com.fancy.module.agent.controller.vo;

import com.fancy.module.agent.repository.pojo.AgMerchant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AgMerchantVo extends AgMerchant {

    /**
     * 代理商户创建人名称（所属代理商）
     */
    private String agUserName;
}
