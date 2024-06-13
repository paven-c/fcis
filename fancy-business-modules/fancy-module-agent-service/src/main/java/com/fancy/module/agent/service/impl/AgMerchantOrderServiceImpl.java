package com.fancy.module.agent.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.exception.NeedLoginException;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.security.core.LoginUser;
import com.fancy.module.agent.controller.req.EditAgMerchantOrderReq;
import com.fancy.module.agent.controller.req.EditAgUserBalanceDetailReq;
import com.fancy.module.agent.controller.req.QueryAgMerchantOrderReq;
import com.fancy.module.agent.controller.vo.AgMerchantOrderOverviewVo;
import com.fancy.module.agent.controller.vo.AgMerchantOrderVo;
import com.fancy.module.agent.convert.merchant.AgMerchantOrderConvert;
import com.fancy.module.agent.enums.AgUserBalanceDetailType;
import com.fancy.module.agent.repository.mapper.*;
import com.fancy.module.agent.repository.pojo.AgMerchant;
import com.fancy.module.agent.repository.pojo.AgMerchantOrder;
import com.fancy.module.agent.repository.pojo.AgMerchantOrderDetail;
import com.fancy.module.agent.service.AgMerchantOrderDetailService;
import com.fancy.module.agent.service.AgMerchantOrderService;
import com.fancy.module.agent.service.AgUserBalanceService;
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import jakarta.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
        //获取登录人Id和部门Id
        return baseMapper.countAgMerchantOrderOverviewVo(creatorIds, null);
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
                .collect(Collectors.toMap(UserRespDTO::getId, UserRespDTO::getUsername));

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
        Optional.ofNullable(agMerchant).orElseThrow(()->new SecurityException("客户不存在"));
        if (ObjectUtil.equals(agMerchant.getCreatorId(), loginUserId)) {
            throw  new  SecurityException("不能创建其他代理商户订单");
        }
        //计算消耗
        builderEditAgMerchantOrder(req);
        //创建订单
        AgMerchantOrder agMerchantOrder = AgMerchantOrderConvert.INSTANCE.convertAgMerchantOrder(req,loginUserId,loginUserDeptId);
        agMerchantOrder.setAgMerchantId(agMerchant.getId()).setName(agMerchant.getName()).setMerchantId(agMerchant.getMerchantId());
        List<AgMerchantOrderDetail> agMerchantOrderDetails = AgMerchantOrderConvert.INSTANCE.convertAgMerchantOrder(req.getOrderDetailList(),loginUserId,loginUserDeptId);
        List<AgMerchantOrder.AgMerchantOrderDetailVo> agMerchantOrderDetailVos = AgMerchantOrderConvert.INSTANCE.convertAgMerchantOrderDetailVo(agMerchantOrderDetails);
        agMerchantOrder.setServiceJson(agMerchantOrderDetailVos);
        save(agMerchantOrder);
        agMerchantOrderDetails.forEach(orderDetail -> orderDetail.setAgMerchantOrderId(agMerchantOrder.getId())
                .setAgMerchantId(agMerchantOrder.getAgMerchantId())
                .setName(agMerchantOrder.getName())
                .setMerchantId(agMerchantOrder.getMerchantId()));
        agMerchantOrderDetailService.saveBatch(agMerchantOrderDetails);
        //扣减代理商户余额
        boolean b = agUserBalanceService.changeBalance(new EditAgUserBalanceDetailReq()
                .setCreateId(loginUserId)
                .setCreateName(nickname)
                .setDeptId(loginUserDeptId)
                .setFromAgUserId(loginUserId)
                .setFromUserName(nickname)
                .setToAgUserId(agMerchant.getId())
                .setToAgUsername(agMerchant.getName())
                .setCheckTo(false)
                .setPrice(agMerchantOrder.getOrderMoney())
                .setObject(agMerchantOrder.getId().toString())
                .setObjectType(AgUserBalanceDetailType.CONTENT_SERVICE_CONSUMPTION)
                .setObjectSubType(req.getOrderType())
                .setObjectSubName(agMerchantOrder.getOrderName())
                .setRemarks("代理商订单创建"));

        if (!b) {
            throw new RuntimeException("扣减代理商户余额失败");
        }

    }

    /**
     * 计算订单金额数据
     */
    private void builderEditAgMerchantOrder(EditAgMerchantOrderReq req){

    }
}
