package com.fancy.module.agent.controller;


import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;
import static com.fancy.common.pojo.CommonResult.success;
import static com.fancy.module.common.enums.ErrorCodeConstants.AGENT_NOT_EXISTS;

import cn.hutool.core.collection.CollUtil;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.common.pojo.CommonResult;
import com.fancy.common.pojo.PageResult;
import com.fancy.component.core.util.ExcelUtils;
import com.fancy.module.agent.controller.vo.AgentPageReqVO;
import com.fancy.module.agent.controller.vo.AgentRespVO;
import com.fancy.module.agent.controller.vo.AgentSaveReqVO;
import com.fancy.module.agent.convert.agent.AgentConvert;
import com.fancy.module.agent.enums.AgentStatusEnum;
import com.fancy.module.agent.repository.pojo.agent.Agent;
import com.fancy.module.agent.service.agent.AgentService;
import com.fancy.module.common.api.dept.DeptApi;
import com.fancy.module.common.api.dept.dto.DeptSaveReqDTO;
import com.fancy.module.common.api.user.UserApi;
import com.fancy.module.common.api.user.dto.UserSaveReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
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

    public static final Long AGENT_ROOT_ID = 1L;

    @Resource
    private UserApi userApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private AgentService agentService;

    @Operation(summary = "新增代理商")
    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('agent:agent:create')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Long> createAgent(@Valid @RequestBody AgentSaveReqVO reqVO) {
        // 不指定父级代理商为一级代理添加、指定为二级代理添加
        Agent parentAgent = getParentAgent(reqVO.getParentAgentId());
        // 父级代理商ID
        Long parentAgentId = Objects.isNull(parentAgent) ? null : parentAgent.getId();
        // 父级代理商部门ID
        Long parentDeptId = Objects.isNull(parentAgent) ? AGENT_ROOT_ID : parentAgent.getDeptId();
        // 新建部门
        Long deptId = deptApi.createDept(DeptSaveReqDTO.builder().name(reqVO.getAgentName()).parentId(parentDeptId).phone(reqVO.getMobile())
                .status(CommonStatusEnum.DISABLE.getStatus()).build());
        // 新增用户
        Long userId = userApi.createUser(UserSaveReqDTO.builder().username(reqVO.getContactorName()).nickname(reqVO.getContactorName()).deptId(deptId)
                .mobile(reqVO.getMobile()).password(reqVO.getPassword()).status(CommonStatusEnum.DISABLE.getStatus()).build());
        // 新增代理商
        reqVO.setDeptId(deptId);
        reqVO.setParentAgentId(parentAgentId);
        reqVO.setStatus(AgentStatusEnum.PENDING_REVIEW.getStatus());
        return success(agentService.createAgent(reqVO));
    }

    @Operation(summary = "编辑代理商")
    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('agengt:agent:update')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> updateAgent(@Valid @RequestBody AgentSaveReqVO reqVO) {
        agentService.updateAgent(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除代理商")
    @Parameter(name = "id", description = "编号", required = true, example = "")
    @PreAuthorize("@ss.hasPermission('agent:agent:delete')")
    public CommonResult<Boolean> deleteAgent(@RequestParam("id") Long id) {
        agentService.deleteAgent(id);
        return success(true);
    }

    @GetMapping("/detail")
    @Operation(summary = "代理商详情")
    @Parameter(name = "id", description = "编号", required = true, example = "")
    @PreAuthorize("@ss.hasPermission('agent:agent:detail')")
    public CommonResult<AgentRespVO> getAgent(@RequestParam("id") Long id) {
        Agent agent = agentService.getAgent(id);
        if (agent == null) {
            return success(null);
        }
        return success(AgentConvert.INSTANCE.convertVO(agent));
    }

    @GetMapping("/page")
    @Operation(summary = "代理商分页列表")
    @PreAuthorize("@ss.hasPermission('agent:agent:list')")
    public CommonResult<PageResult<AgentRespVO>> getAgentPage(@Valid AgentPageReqVO pageReqVO) {
        // 代理商分页列表
        PageResult<Agent> pageResult = agentService.getAgentPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        return success(new PageResult<>(AgentConvert.INSTANCE.convertList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export")
    @Operation(summary = "导出代理商")
    @PreAuthorize("@ss.hasPermission('agent:agent:export')")
    public void exportAgentList(@Validated AgentPageReqVO exportReqVO, HttpServletResponse response) throws IOException {
        List<Agent> list = agentService.getAgentPage(exportReqVO).getList();
        ExcelUtils.write(response, "代理商数据.xls", "代理商", AgentRespVO.class, AgentConvert.INSTANCE.convertList(list));
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
