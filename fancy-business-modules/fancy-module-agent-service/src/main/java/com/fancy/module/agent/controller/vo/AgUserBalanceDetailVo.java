package com.fancy.module.agent.controller.vo;

import com.fancy.module.agent.repository.pojo.AgUserBalanceDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AgUserBalanceDetailVo extends AgUserBalanceDetail {

}
