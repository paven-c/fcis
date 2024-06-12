package com.fancy.module.agent.controller.req;

import com.fancy.module.agent.repository.pojo.AgMerchant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EditAgMerchantReq extends AgMerchant {
}
