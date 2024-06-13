package com.fancy.module.agent.service.impl;

import static com.fancy.component.redis.constant.RedisConstant.AG_USER_CHANGE_BALANCE;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.enums.DeleteStatusEnum;
import com.fancy.module.agent.controller.req.EditAgUserBalanceDetailReq;
import com.fancy.module.agent.convert.balance.AgUserBalanceConvert;
import com.fancy.module.agent.enums.AgUserBalanceDetailBillType;
import com.fancy.module.agent.repository.mapper.AgUserBalanceMapper;
import com.fancy.module.agent.repository.pojo.AgUserBalance;
import com.fancy.module.agent.repository.pojo.AgUserBalanceDetail;
import com.fancy.module.agent.service.AgUserBalanceDetailService;
import com.fancy.module.agent.service.AgUserBalanceService;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单扣减流水表 服务实现类
 * </p>
 *
 * @author xingchen
 * @since 2024-06-07
 */
@Service
@Slf4j
public class AgUserBalanceServiceImpl extends ServiceImpl<AgUserBalanceMapper, AgUserBalance> implements AgUserBalanceService {

    @Resource
    private AgUserBalanceDetailService agUserBalanceDetailService;
    @Resource
    private AgUserBalanceMapper userBalanceMapper;

    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional
    public boolean changeBalance(EditAgUserBalanceDetailReq req) {
        //分布式锁 变更用户Id
        RLock to = redissonClient.getLock(AG_USER_CHANGE_BALANCE + req.getToAgUserId());
        RLock from = redissonClient.getLock(AG_USER_CHANGE_BALANCE + req.getFromAgUserId());
        if (to.tryLock() && from.tryLock()) {
            try {
                LocalDateTime now = LocalDateTime.now();
                List<AgUserBalanceDetail> agUserBalanceDetailList = new ArrayList<>();
                //查询入账用户余额
                if (req.getCheckTo()) {
                    AgUserBalance accounting = lambdaQuery()
                            .eq(AgUserBalance::getAgUserId, req.getToAgUserId())
                            .eq(AgUserBalance::getDeleted, 0)
                            .last("limit 1").one();
                    Optional.ofNullable(accounting).orElseThrow(() -> new SecurityException("用户不存在"));
                    Optional.ofNullable(req.getObjectType()).orElseThrow(() -> new SecurityException("未知的变更类型"));
                    //入账
                    //变更后金额
                    BigDecimal add = accounting.getNowPrice().add(req.getPrice());
                    //更新
                    int i = agUserBalanceDetailService.updateBalance(accounting.getId(), req.getPrice(), AgUserBalanceDetailBillType.PAYMENT_OUT);
                    if (i < 1) {
                        throw new SecurityException("更新失败");
                    }
                    //入账明细
                    AgUserBalanceDetail agUserBalanceDetail = AgUserBalanceConvert.INSTANCE.convertAgUserBalanceDetail(req, AgUserBalanceDetailBillType.PAYMENT_OUT.getType(), accounting.getNowPrice(), add);
                    agUserBalanceDetail.setCreateTime(now);
                    agUserBalanceDetail.setUpdateTime(now);
                    agUserBalanceDetailList.add(agUserBalanceDetail);
                }
                //查询出账用户余额
                if (req.getCheckFrom()) {
                    AgUserBalance paymentOut = lambdaQuery()
                            .eq(AgUserBalance::getAgUserId, req.getFromAgUserId())
                            .last("limit 1").one();
                    Optional.ofNullable(paymentOut).orElseThrow(() -> new SecurityException("用户不存在"));
                    //检查余额是否充足
                    if (paymentOut.getNowPrice().compareTo(req.getPrice()) < 0) {
                        throw new SecurityException("余额不足");
                    }
                    //更新
                    int i = agUserBalanceDetailService.updateBalance(paymentOut.getId(), req.getPrice(), AgUserBalanceDetailBillType.ACCOUNTING);
                    if (i < 1) {
                        throw new SecurityException("更新失败");
                    }
                    //出账
                    //变更后金额
                    BigDecimal sub = paymentOut.getNowPrice().subtract(req.getPrice());
                    //出账明细
                    Long fromAgUserId = req.getFromAgUserId();
                    Long toAgUserId = req.getToAgUserId();
                    req.setToAgUserId(fromAgUserId);
                    req.setFromAgUserId(toAgUserId);
                    AgUserBalanceDetail agUserBalanceDetail1 = AgUserBalanceConvert.INSTANCE.convertAgUserBalanceDetail(req, AgUserBalanceDetailBillType.ACCOUNTING.getType(), paymentOut.getNowPrice(), sub);
                    agUserBalanceDetail1.setCreateTime(now);
                    agUserBalanceDetail1.setUpdateTime(now);
                    agUserBalanceDetailList.add(agUserBalanceDetail1);
                }
                if (ObjectUtil.isNotEmpty(agUserBalanceDetailList)) {
                    agUserBalanceDetailService.saveBatch(agUserBalanceDetailList);
                }
                return true;
            } catch (Exception e) {
                log.error("修改用户余额异常,params:{},error:{}", JSONUtil.toJsonStr(req), e.getMessage(), e);
                throw new SecurityException(e);
            } finally {
                to.unlock();
                from.unlock();
            }
        }
        return false;

    }

    @Override
    public void createUserBalance(Long userId) {
        AgUserBalance userBalance = getByUserId(userId);
        if (Objects.isNull(userBalance)) {
            userBalance = new AgUserBalance().setAgUserId(userId).setNowPrice(BigDecimal.ZERO).setStatus(CommonStatusEnum.ENABLE.getStatus())
                    .setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now()).setDeleted(DeleteStatusEnum.ACTIVATED.getStatus());
            userBalanceMapper.insert(userBalance);
        }
    }

    @Override
    public AgUserBalance getUserBalance(Long userId) {
        return getByUserId(userId);
    }

    private AgUserBalance getByUserId(Long userId) {
        return userBalanceMapper.selectOne(AgUserBalance::getAgUserId, userId, AgUserBalance::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus());
    }
}
