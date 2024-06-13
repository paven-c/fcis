package com.fancy.module.agent.controller.req;

import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EditAgMerchantOrderReq  {
    /**
     * 代理商户id
     */
    @NotNull(message = "代理商户id不能为空")
    private Long agMerchantId;

    /**
     * 代理商户名称
     */
    @NotBlank(message = "代理商户名称不能为空")
    private String name;

    /**
     * fc商户id
     */
    @NotNull(message = "fc商户id不能为空")
    private Long merchantId;


    /**
     * 订单明细
     */
    @NotEmpty(message = "订单明细不能为空")
    private List<@Valid OrderDetail> orderDetailList;

    /**
     * 订单总金额
     */
    private BigDecimal orderMoney;

    /**
     * 服务总任务数
     */
    private Integer serviceTotalNum;

    /**
     * 订单名称
     */
    @NotBlank(message = "订单名称不能为空")
    private String orderName;

    /**
     * 下单方式 0套餐 1服务内容
     */
    @NotNull(message = "下单方式不能为空")
    private Integer orderType;

    /**
     * 套餐服务内容
     */
    private List<AgMerchantOrder.AgMerchantOrderDetailVo> serviceJson = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = false)
    public static class OrderDetail{
        /**
         * 服务版本类型
         */
        @NotNull(message = "服务版本Id不能为空")
        private Long contentServiceId;
        /**
         * 服务类型
         */
        @NotNull(message = "服务类型不能为空")
        private Integer serviceType;
        /**
         * 覆盖数
         */
        @NotNull(message = "覆盖数不能为空")
        private Integer coverageNumber;
        /**
         * 生成数
         */
        @NotNull(message = "生成数不能为空")
        private Integer numberOfGenerations;

        /**
         * 订单金额
         */
        private BigDecimal orderSubMoney;
        /**
         * 订单单价
         */
        private  BigDecimal orderUnitPrice;

        /**
         * 服务任务数
         */
        private Integer serviceTotalNum;

        /**
         * 订单名称
         */
        private String orderName;
    }
}
