package com.fancy.module.common.req;

import com.fancy.module.common.repository.pojo.AgMerchant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EditAgMerchantReq extends AgMerchant {
}
