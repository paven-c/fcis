package com.fancy.module.agent.controller;


import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.common.pojo.CommonResult.success;
import static com.fancy.component.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_EXITS_CHILDREN;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_LEVEL_NOT_SECOND;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_NOT_EXISTS;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_STATUS_NOT_ACTIVITY;
import static com.fancy.module.common.enums.ErrorCodeConstants.USER_NOT_EXISTS;

import cn.hutool.core.collection.CollUtil;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.core.util.ExcelUtils;
import com.fancy.module.agent.controller.req.EditAgUserBalanceDetailReq;
import com.fancy.module.agent.controller.req.QueryAgUserBalanceDetailReq;
import com.fancy.module.agent.controller.vo.AgUserBalanceDetailVo;
import com.fancy.module.agent.controller.vo.AgentPageReqVO;
import com.fancy.module.agent.controller.vo.AgentRechargeReqVO;
import com.fancy.module.agent.controller.vo.AgentRespVO;
import com.fancy.module.agent.controller.vo.AgentSaveReqVO;
import com.fancy.module.agent.convert.agent.AgentConvert;
import com.fancy.module.agent.enums.AgUserBalanceDetailType;
import com.fancy.module.agent.enums.AgentLevelEnum;
import com.fancy.module.agent.enums.AgentStatusEnum;
import com.fancy.module.agent.repository.pojo.agent.Agent;
import com.fancy.module.agent.service.AgUserBalanceDetailService;
import com.fancy.module.agent.service.AgUserBalanceService;
import com.fancy.module.agent.service.agent.AgentService;
import com.fancy.module.common.api.dept.DeptApi;
import com.fancy.module.common.api.dept.dto.DeptSaveReqDTO;
import com.fancy.module.common.api.permission.PermissionApi;
import com.fancy.module.common.api.permission.RoleApi;
import com.fancy.module.common.api.permission.dto.RoleRespDTO;
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserRespDTO;
import com.fancy.module.common.api.user.dto.UserSaveReqDTO;
import com.fancy.module.common.enums.permission.RoleCodeEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author paven
 */
@Tag(name = "代理商")
@RestController
@RequestMapping("/agent")
@Validated
public class AgentController {

    public static final Long ROOT_DEPT_ID = 1L;

    @Resource
    private UserApi userApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private RoleApi roleApi;
    @Resource
    private AgentService agentService;
    @Resource
    private PermissionApi permissionApi;
    @Resource
    private AgUserBalanceService userBalanceService;
    @Resource
    private AgUserBalanceDetailService agUserBalanceDetailService;

    @Operation(summary = "新增代理商")
    @PostMapping("/create")
    @PreAuthorize("@ss.hasAnyPermissions('agent:agent:create-first-level','agent:agent:create-second-level')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Long> createAgent(@Valid @RequestBody AgentSaveReqVO reqVO) {
        // 不指定父级代理商为一级代理添加、指定为二级代理添加
        Agent parentAgent = getParentAgent(reqVO.getParentAgentId());
        // 新建部门
        Long deptId = deptApi.createDept(DeptSaveReqDTO.builder().name(reqVO.getAgentName()).phone(reqVO.getMobile())
                .parentId(Objects.isNull(parentAgent) ? ROOT_DEPT_ID : parentAgent.getDeptId()).status(CommonStatusEnum.DISABLE.getStatus()).build());
        // 新增用户
        Long userId = userApi.createUser(UserSaveReqDTO.builder().username(reqVO.getMobile()).nickname(reqVO.getContactorName()).deptId(deptId)
                .mobile(reqVO.getMobile()).password(reqVO.getPassword()).status(CommonStatusEnum.DISABLE.getStatus()).build());
        // 设置用户角色
        RoleRespDTO role = roleApi.getRoleByCode(
                Objects.isNull(parentAgent) ? RoleCodeEnum.FIRST_LEVEL_AGENT.getCode() : RoleCodeEnum.SECOND_LEVEL_AGENT.getCode());
        permissionApi.assignUserRole(userId, Sets.newHashSet(role.getId()));
        // 新增用户账户
        userBalanceService.createUserBalance(userId);
        // 新增代理商
        reqVO.setUserId(userId);
        reqVO.setDeptId(deptId);
        reqVO.setParentAgentId(Objects.isNull(parentAgent) ? null : parentAgent.getId());
        reqVO.setLevel(Objects.isNull(parentAgent) ? AgentLevelEnum.FIRST_LEVEL.getType() : AgentLevelEnum.SECOND_LEVEL.getType());
        reqVO.setStatus(AgentStatusEnum.PENDING_REVIEW.getStatus());
        return success(agentService.createAgent(reqVO));
    }

    @Operation(summary = "编辑代理商")
    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('agengt:agent:update')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> updateAgent(@Valid @RequestBody AgentSaveReqVO reqVO) {
        // 编辑代理商
        agentService.updateAgent(reqVO);
        // 获取代理商
        Agent agent = agentService.getAgent(reqVO.getId());
        // 编辑部门
        deptApi.updateDept(DeptSaveReqDTO.builder().id(agent.getDeptId()).name(reqVO.getAgentName()).phone(reqVO.getMobile()).build());
        // 编辑用户
        userApi.updateUser(UserSaveReqDTO.builder().id(agent.getUserId())
                .username(reqVO.getMobile()).nickname(reqVO.getAgentName()).mobile(reqVO.getMobile()).build());
        return success(true);
    }

    @Operation(summary = "删除代理商")
    @Parameter(name = "id", description = "编号", required = true, example = "")
    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('agent:agent:delete')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> deleteAgent(@RequestParam("id") Long id) {
        // 获取代理商
        Agent agent = agentService.getAgent(id);
        if (Objects.isNull(agent)) {
            throw exception(AGENT_NOT_EXISTS);
        }
        // 查询子级代理商
        if (agentService.selectCountByParentId(id) > 0) {
            throw exception(AGENT_EXITS_CHILDREN);
        }
        // 禁用用户
        userApi.updateUser(UserSaveReqDTO.builder().id(agent.getUserId()).status(CommonStatusEnum.DISABLE.getStatus()).build());
        // 删除代理商
        agentService.deleteAgent(agent.getId());
        return success(true);
    }

    @Operation(summary = "代理商详情")
    @Parameter(name = "id", description = "编号", required = true, example = "")
    @GetMapping("/detail")
    @PreAuthorize("@ss.hasAnyPermissions('agent:agent:list','agent:agent:detail')")
    public CommonResult<AgentRespVO> getAgent(@RequestParam("id") Long id) {
        Agent agent = agentService.getAgent(id);
        if (agent == null) {
            return success(null);
        }
        Map<Long, String> agentNames = Optional.ofNullable(agentService.selectByIds(Sets.newHashSet(agent.getParentId()))).orElse(Lists.newArrayList()).stream()
                .collect(Collectors.toMap(Agent::getId, Agent::getAgentName));
        return success(AgentConvert.INSTANCE.convertVO(agent, agentNames));
    }

    @Operation(summary = "代理商审核通过")
    @PutMapping("/approve")
    @PreAuthorize("@ss.hasPermission('agengt:agent:approve')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> approveAgent(@RequestParam("agentId") Long agentId) {
        // 获取代理商
        Agent agent = agentService.getAgent(agentId);
        if (Objects.isNull(agent)) {
            throw exception(AGENT_NOT_EXISTS);
        }
        // 审批代理商
        agentService.updateAgent(
                AgentSaveReqVO.builder().id(agent.getId()).status(AgentStatusEnum.APPROVED.getStatus()).approveTime(LocalDateTime.now()).build());
        // 启用部门
        deptApi.updateDept(DeptSaveReqDTO.builder().id(agent.getDeptId()).status(CommonStatusEnum.ENABLE.getStatus()).build());
        // 启用用户
        userApi.updateUser(UserSaveReqDTO.builder().id(agent.getUserId()).status(CommonStatusEnum.ENABLE.getStatus()).build());
        return success(true);
    }

    @Operation(summary = "代理商审核拒绝")
    @PutMapping("/reject")
    @PreAuthorize("@ss.hasPermission('agengt:agent:reject')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> rejectAgent(@RequestParam("agentId") Long agentId) {
        // 获取代理商
        Agent agent = agentService.getAgent(agentId);
        if (Objects.isNull(agent)) {
            throw exception(AGENT_NOT_EXISTS);
        }
        // 拒绝代理商
        agentService.updateAgent(AgentSaveReqVO.builder().id(agent.getId()).status(AgentStatusEnum.REJECTED.getStatus()).build());
        return success(true);
    }

    @Operation(summary = "财务充值一级代理商")
    @PostMapping("/recharge-first-level")
    @PreAuthorize("@ss.hasPermission('agent:agent:recharge-first-level')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> rechargeAgent(@Valid @RequestBody AgentRechargeReqVO reqVO) {
        UserRespDTO user = Optional.ofNullable(userApi.getUser(getLoginUserId())).orElseThrow(() -> exception(USER_NOT_EXISTS));
        // 获取目标代理商
        Agent targetAgent = agentService.getAgent(reqVO.getAgentId());
        if (Objects.isNull(targetAgent)) {
            throw exception(AGENT_NOT_EXISTS);
        }
        // 校验代理商状态
        if (!AgentStatusEnum.isActivityStatus(targetAgent.getStatus())) {
            throw exception(AGENT_STATUS_NOT_ACTIVITY);
        }
        // 校验财务角色
        boolean isFinance = permissionApi.hasAnyRoles(user.getId(), RoleCodeEnum.FINANCE.getCode());
        if (!isFinance) {
            throw new AccessDeniedException("仅限财务操作");
        }
        // 转账操作
        userBalanceService.changeBalance(new EditAgUserBalanceDetailReq()
                .setFromAgUserId(user.getId()).setFromUserName(user.getNickname())
                .setToAgUserId(targetAgent.getUserId()).setToAgUsername(targetAgent.getAgentName()).setCheckFrom(false)
                .setPrice(new BigDecimal(reqVO.getAmount())).setObjectType(AgUserBalanceDetailType.FIRST_LEVEL_AGENT_RECHARGE)
                .setCreateId(user.getId()).setCreateName(user.getNickname())
                .setDeptId(user.getDeptId()).setRemarks(reqVO.getRemarks()));
        return success(true);
    }

    @Operation(summary = "充值二级代理商")
    @PostMapping("/recharge-second-level")
    @PreAuthorize("@ss.hasPermission('agent:agent:recharge-second-level')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> rechargeSecondAgent(@Valid @RequestBody AgentRechargeReqVO reqVO) {
        UserRespDTO user = Optional.ofNullable(userApi.getUser(getLoginUserId())).orElseThrow(() -> exception(USER_NOT_EXISTS));
        // 获取目标代理商
        Agent targetAgent = agentService.getAgent(reqVO.getAgentId());
        if (Objects.isNull(targetAgent)) {
            throw exception(AGENT_NOT_EXISTS);
        }
        // 校验代理商状态
        if (!AgentStatusEnum.isActivityStatus(targetAgent.getStatus())) {
            throw exception(AGENT_STATUS_NOT_ACTIVITY);
        }
        // 校验二级代理
        if (!AgentLevelEnum.SECOND_LEVEL.getType().equals(targetAgent.getLevel())) {
            throw exception(AGENT_LEVEL_NOT_SECOND);
        }
        // 校验财务角色
        boolean isFinance = permissionApi.hasAnyRoles(user.getId(), RoleCodeEnum.FINANCE.getCode());
        // 校验一级代理商角色
        boolean isFirstLevelAgent = permissionApi.hasAnyRoles(user.getId(), RoleCodeEnum.FIRST_LEVEL_AGENT.getCode());
        if (!isFirstLevelAgent && !isFinance) {
            throw new AccessDeniedException("无操作权限");
        }
        // 转账操作
        userBalanceService.changeBalance(new EditAgUserBalanceDetailReq()
                .setFromAgUserId(user.getId()).setFromUserName(user.getNickname())
                .setToAgUserId(targetAgent.getUserId()).setToAgUsername(targetAgent.getAgentName()).setCheckFrom(!isFinance)
                .setPrice(new BigDecimal(reqVO.getAmount())).setObjectType(AgUserBalanceDetailType.SECONDARY_AGENT_RECHARGE)
                .setCreateId(user.getId()).setCreateName(user.getNickname())
                .setDeptId(user.getDeptId()).setRemarks(reqVO.getRemarks()));
        return success(true);
    }

    @Operation(summary = "代理商分页列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('agent:agent:list')")
    public CommonResult<PageResult<AgentRespVO>> getAgentPage(@Valid AgentPageReqVO pageReqVO) {
        UserRespDTO user = Optional.ofNullable(userApi.getUser(getLoginUserId())).orElseThrow(() -> exception(USER_NOT_EXISTS));
        // 我司角色
        boolean isCompanyRole = permissionApi.hasAnyRoles(
                user.getId(), RoleCodeEnum.SUPER_ADMIN.getCode(), RoleCodeEnum.AGENT_MANAGER.getCode(), RoleCodeEnum.OPERATIONS_STAFF.getCode(),
                RoleCodeEnum.FINANCE.getCode());
        pageReqVO.setIsCompanyRole(isCompanyRole);
        // 代理商分页列表
        PageResult<Agent> pageResult = agentService.getAgentPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal(), pageResult.getPageNum(), pageResult.getPageSize()));
        }
        Set<Long> parenAgentIds = pageResult.getList().stream().map(Agent::getParentId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> agentNames = Maps.newHashMap();
        if (CollUtil.isNotEmpty(parenAgentIds)) {
            agentNames = Optional.ofNullable(agentService.selectByIds(parenAgentIds)).orElse(Lists.newArrayList()).stream()
                    .collect(Collectors.toMap(Agent::getId, Agent::getAgentName));
        }
        return success(new PageResult<>(AgentConvert.INSTANCE.convertList(pageResult.getList(), agentNames), pageResult.getTotal(), pageResult.getPageNum(),
                pageResult.getPageSize()));
    }

    @Operation(summary = "导出代理商")
    @GetMapping("/export")
    @PreAuthorize("@ss.hasPermission('agent:agent:export')")
    public void exportAgentList(@Validated AgentPageReqVO exportReqVO, HttpServletResponse response) throws IOException {
        List<Agent> list = agentService.getAgentPage(exportReqVO).getList();
        Set<Long> parenAgentIds = list.stream().map(Agent::getParentId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> agentNames = Maps.newHashMap();
        if (CollUtil.isNotEmpty(parenAgentIds)) {
            agentNames = Optional.ofNullable(agentService.selectByIds(parenAgentIds)).orElse(Lists.newArrayList()).stream()
                    .collect(Collectors.toMap(Agent::getId, Agent::getAgentName));
        }
        ExcelUtils.write(response, "agent-data.xlsx", "agent", AgentRespVO.class, AgentConvert.INSTANCE.convertList(list, agentNames));
    }

    @Operation(summary = "我的交易")
    @PostMapping("/myTransactionPageList")
    public CommonResult<PageResult<AgUserBalanceDetailVo>> myTransactionPageList(@RequestBody QueryAgUserBalanceDetailReq req) {
        Set<String> roleCodes = permissionApi.getUserRoleCodeListByUserIds(getLoginUserId());
        // 是否代理商角色
        boolean b = roleCodes.stream().anyMatch(s -> RoleCodeEnum.getAgent().contains(s));
        req.setCreatorIds(b ? Collections.singletonList(getLoginUserId()) : null);
        return CommonResult.success(agUserBalanceDetailService.myTransactionPageList(req));
    }

    /**
     * 获取父级代理商
     *
     * @param agentId 代理商ID
     * @return 代理商
     */
    private Agent getParentAgent(Long agentId) {
        if (agentId == null) {
            return null;
        }
        Agent agent = agentService.selectById(agentId);
        if (agent == null) {
            throw exception(AGENT_NOT_EXISTS);
        }
        return agent;
    }
}
