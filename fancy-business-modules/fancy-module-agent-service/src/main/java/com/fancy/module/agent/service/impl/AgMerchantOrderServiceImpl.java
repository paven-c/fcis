package com.fancy.module.agent.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.exception.NeedLoginException;
import com.fancy.common.exception.ServiceException;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.security.core.LoginUser;
import com.fancy.module.agent.controller.req.EditAgMerchantOrderReq;
import com.fancy.module.agent.controller.req.EditAgUserBalanceDetailReq;
import com.fancy.module.agent.controller.req.QueryAgMerchantOrderReq;
import com.fancy.module.agent.controller.vo.AgMerchantOrderOverviewVo;
import com.fancy.module.agent.controller.vo.AgMerchantOrderVo;
import com.fancy.module.agent.convert.merchant.AgMerchantOrderConvert;
import com.fancy.module.agent.enums.AgUserBalanceDetailType;
import com.fancy.module.agent.enums.ContentFortTypeEnum;
import com.fancy.module.agent.repository.mapper.*;
import com.fancy.module.agent.repository.pojo.*;
import com.fancy.module.agent.service.AgMerchantOrderDetailService;
import com.fancy.module.agent.service.AgMerchantOrderService;
import com.fancy.module.agent.service.AgUserBalanceService;
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fancy.component.security.core.util.SecurityFrameworkUtils.*;

/**
 * <p>
 * 订单扣减流水表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
public class AgMerchantOrderServiceImpl extends ServiceImpl<AgMerchantOrderMapper, AgMerchantOrder> implements AgMerchantOrderService {

    @Resource
    private AgMerchantOrderDetailService agMerchantOrderDetailService;

    @Resource
    private AgUserBalanceService agUserBalanceService;

    @Resource
    private AgMerchantMapper agMerchantMapper;
    @Resource
    private AgContentServiceMainMapper agContentServiceMainMapper;
    @Resource
    private AgContentServiceDetailMapper agContentServiceDetailMapper;

    @Resource
    private UserApi userApi;


    @Override
    public AgMerchantOrderOverviewVo overview(List<Long> creatorIds) {
        //获取登录人Id
        return baseMapper.countAgMerchantOrderOverviewVo(creatorIds);
    }

    @Override
    public PageResult<AgMerchantOrderVo> pageList(QueryAgMerchantOrderReq req) {
        PageResult<AgMerchantOrder> agMerchantOrderPageResult = baseMapper.selectPage(req, Wrappers.lambdaQuery(AgMerchantOrder.class)
                .eq(ObjectUtil.isNotEmpty(req.getAgMerchantId()), AgMerchantOrder::getAgMerchantId, req.getAgMerchantId())
                .in(ObjectUtil.isNotEmpty(req.getCreatorIds()), AgMerchantOrder::getCreatorId, req.getCreatorIds())
                .between(
                        ObjectUtil.isNotEmpty(req.getStartTime()) && ObjectUtil.isNotEmpty(req.getEndTime()), AgMerchantOrder::getCreateTime,
                        req.getStartTime(), req.getEndTime())
                .eq(AgMerchantOrder::getDeleted, 0)
                .orderByDesc(AgMerchantOrder::getCreateTime));
        if (ObjectUtil.isEmpty(agMerchantOrderPageResult.getList())) {
            return new PageResult<>(agMerchantOrderPageResult.getTotal(), req.getPageNum(), req.getPageSize());
        }
        List<AgMerchantOrderVo> agMerchantOrderVos = AgMerchantOrderConvert.INSTANCE.convertAgMerchantOrderVo(agMerchantOrderPageResult.getList());
        List<Long> collect = agMerchantOrderVos.stream()
                .map(AgMerchantOrderVo::getCreatorId)
                .filter(ObjectUtil::isNotEmpty)
                .collect(Collectors.toList());
        Map<Long, String> userMap = userApi.getUserByIds(collect)
                .stream()
                .collect(Collectors.toMap(UserRespDTO::getId, UserRespDTO::getNickname));

        agMerchantOrderVos.forEach(agMerchantOrderVo -> {
            if (ObjectUtil.isNotEmpty(agMerchantOrderVo.getCreatorId())) {
                agMerchantOrderVo.setAgUserName(userMap.get(agMerchantOrderVo.getCreatorId()));
            }
        });
        return new PageResult<>(agMerchantOrderVos, agMerchantOrderPageResult.getTotal(), req.getPageNum(), req.getPageSize());
    }

    @Override
    @Transactional
    public void add(EditAgMerchantOrderReq req) {
        LoginUser loginUser = Optional.ofNullable(getLoginUser()).orElseThrow(NeedLoginException::new);
        Long loginUserId = loginUser.getId();
        Long loginUserDeptId = MapUtil.getLong(loginUser.getInfo(), LoginUser.INFO_KEY_DEPT_ID, null);
        String nickname = MapUtil.getStr(loginUser.getInfo(), LoginUser.INFO_KEY_NICKNAME, "");
        //查询商户代理商是否登录id 不是登录人不能创建
        AgMerchant agMerchant = agMerchantMapper.selectById(req.getAgMerchantId());
        Optional.ofNullable(agMerchant).orElseThrow(()->new ServiceException("客户不存在"));
        if (!ObjectUtil.equals(agMerchant.getCreatorId(), loginUserId)) {
            throw  new ServiceException("不能创建其他代理客户订单");
        }
        //计算消耗
        builderEditAgMerchantOrder(req);
        //创建订单
        AgMerchantOrder agMerchantOrder = AgMerchantOrderConvert.INSTANCE.convertAgMerchantOrder(req,loginUserId,loginUserDeptId);
        agMerchantOrder.setServiceConsumeNum(0L).setAgMerchantId(agMerchant.getId()).setName(agMerchant.getName()).setMerchantId(agMerchant.getMerchantId());
        List<AgMerchantOrderDetail> agMerchantOrderDetails = AgMerchantOrderConvert.INSTANCE.convertAgMerchantOrder(req.getServiceJson());
        save(agMerchantOrder);
        agMerchantOrderDetails.forEach(orderDetail -> orderDetail.setAgMerchantOrderId(agMerchantOrder.getId())
                .setServiceConsumeNum(0L)
                .setAgMerchantId(agMerchantOrder.getAgMerchantId())
                .setName(agMerchantOrder.getName())
                .setMerchantId(agMerchantOrder.getMerchantId())
                .setDeptId(loginUserDeptId));
        agMerchantOrderDetailService.saveBatch(agMerchantOrderDetails);
        //扣减代理商户余额
        boolean b = agUserBalanceService.changeBalance(new EditAgUserBalanceDetailReq()
                .setCreateId(loginUserId)
                .setCreateName(nickname)
                .setFromAgUserId(loginUserId)
                .setFromUserName(nickname)
                .setToAgUserId(agMerchant.getId())
                .setToAgUsername(agMerchant.getName())
                .setCheckTo(false)
                .setPrice(agMerchantOrder.getOrderMoney())
                .setObjectId(agMerchantOrder.getId().toString())
                .setObjectType(AgUserBalanceDetailType.CONTENT_SERVICE_CONSUMPTION)
                .setObjectSubType(req.getOrderType())
                .setObjectSubTypeName(req.getOrderName())
                .setObjectSubTypeDetailName(agMerchantOrder.getServiceJson())
                .setRemarks("代理商创建客户订单"));

        if (!b) {
            throw new RuntimeException("扣减代理商余额失败");
        }

    }

    /**
     * 计算订单金额数据
     */
    private void builderEditAgMerchantOrder(EditAgMerchantOrderReq req){

        List<EditAgMerchantOrderReq.OrderDetail> orderDetailList = req.getOrderDetailList();
        Optional.ofNullable(orderDetailList).orElseThrow(()->new ServiceException("订单详情不能为空"));
        ContentFortTypeEnum contentFortTypeEnum = Optional.ofNullable(ContentFortTypeEnum.getByType(req.getOrderType())).orElseThrow(() -> new ServiceException("订单类型不存在"));
        //订单明细 一条一条计算
        switch (contentFortTypeEnum) {
            case PACKAGE -> {//套餐类型
                for (EditAgMerchantOrderReq.OrderDetail orderDetail : orderDetailList) {
                    AgContentServiceMain agContentServiceMains = agContentServiceMainMapper.selectById(orderDetail.getContentServiceId());
                    //套餐内容信息
                    List<AgContentServiceDetail> agContentServiceDetails = agContentServiceDetailMapper.selectList(Wrappers.lambdaQuery(AgContentServiceDetail.class)
                            .eq(AgContentServiceDetail::getMainId, orderDetail.getContentServiceId())
                            .eq(AgContentServiceDetail::getDeleted, 0));
                    Optional.ofNullable(agContentServiceMains).orElseThrow(() -> new ServiceException("服务内容不存在"));
                    //内容对应的信息
                    Set<Long> collect = agContentServiceDetails.stream().map(AgContentServiceDetail::getContentId).collect(Collectors.toSet());
                    Map<Long, AgContentServiceMain> agContentServiceMainMap = agContentServiceMainMapper.selectBatchIds(collect)
                            .stream().collect(Collectors.toMap(AgContentServiceMain::getId, Function.identity(), (v1, v2) -> v1));
                    if (ObjectUtil.isEmpty(agContentServiceMainMap)) {
                        throw new ServiceException("服务内容不存在");
                    }
                    req.setOrderName(contentFortTypeEnum.getName())
                            .getServiceJson().addAll(agContentServiceDetails.stream().map(a -> {
                                return new AgMerchantOrder.AgMerchantOrderDetailVo()
                                        .setServiceName(agContentServiceMainMap.get(a.getContentId()).getContentName())
                                        .setServiceType(agContentServiceMainMap.get(a.getContentId()).getContentType())
                                        .setContentServiceId(agContentServiceMainMap.get(a.getContentId()).getId())
                                        .setPackageContentServiceId(orderDetail.getContentServiceId())
                                        .setServiceTotalNum(NumberUtil.mul(a.getCoverageNum(), a.getCoverageSkuNum()).intValue())
                                        .setCoverageNum(a.getCoverageNum())
                                        .setCoverageSkuNum(a.getCoverageSkuNum());

                            }).toList());
                    //计算任务数
                    Integer serviceTotalNum = agContentServiceDetails.stream()
                            .map(agContentServiceDetail -> NumberUtil.mul(agContentServiceDetail.getCoverageNum(), agContentServiceDetail.getCoverageSkuNum()).intValue())
                            .reduce(Integer::sum).orElse(0);
                    orderDetail.setServiceTotalNum(serviceTotalNum)
                            .setOrderUnitPrice(BigDecimal.valueOf(agContentServiceMains.getConsumePoint()))
                            .setOrderMoney(BigDecimal.valueOf(agContentServiceMains.getConsumePoint()))
                            .setOrderName(agContentServiceMains.getContentName())
                            .setServiceType(agContentServiceMains.getContentType());
                }
            }
            case CONTENT -> { //服务内容
                for (EditAgMerchantOrderReq.OrderDetail orderDetail : orderDetailList) {
                    AgContentServiceMain agContentServiceMain = agContentServiceMainMapper.selectById(orderDetail.getContentServiceId());
                    Optional.ofNullable(agContentServiceMain).orElseThrow(() -> new ServiceException("服务内容不存在"));
                    Optional.ofNullable(orderDetail.getCoverageNumber()).orElseThrow(() -> new ServiceException("服务覆盖数不能为空"));
                    Optional.ofNullable(orderDetail.getNumberOfGenerations()).orElseThrow(() -> new ServiceException("生成数不能为空"));
                    //计算总数
                    int i = NumberUtil.mul(orderDetail.getNumberOfGenerations(), orderDetail.getCoverageNumber()).intValue();
                    orderDetail.setServiceTotalNum(i)
                            .setOrderUnitPrice(BigDecimal.valueOf(agContentServiceMain.getConsumePoint()))
                            .setOrderMoney(NumberUtil.mul(BigDecimal.valueOf(agContentServiceMain.getConsumePoint()), BigDecimal.valueOf(i)))
                            .setOrderName(agContentServiceMain.getContentName())
                            .setServiceType(agContentServiceMain.getContentType());

                    req.setOrderName(contentFortTypeEnum.getName())
                            .getServiceJson().add(new AgMerchantOrder.AgMerchantOrderDetailVo()
                                    .setServiceName(orderDetail.getOrderName())
                                    .setOrderMoney(orderDetail.getOrderMoney())
                                    .setOrderUnitPrice(orderDetail.getOrderUnitPrice())
                                    .setServiceType(orderDetail.getServiceType())
                                    .setContentServiceId(orderDetail.getContentServiceId())
                                    .setServiceTotalNum(orderDetail.getServiceTotalNum())
                                    .setCoverageNum(orderDetail.getNumberOfGenerations())
                                    .setCoverageSkuNum(orderDetail.getCoverageNumber()));
                }
            }
            default -> throw new ServiceException("订单类型错误");
        }
        //总金额 总任务数
        BigDecimal orderSubMoney = orderDetailList.stream()
                .map(EditAgMerchantOrderReq.OrderDetail::getOrderMoney)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        int sum = orderDetailList.stream()
                .mapToInt(EditAgMerchantOrderReq.OrderDetail::getServiceTotalNum)
                .sum();
        req.setOrderMoney(orderSubMoney)
                .setServiceTotalNum(sum);
    }
}
