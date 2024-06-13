package com.fancy.module.agent.repository.pojo.agent;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fancy.component.mybatis.core.dataobject.BasePojo;
import com.fancy.module.agent.enums.AgentStatusEnum;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author paven
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Accessors(chain = true)
@TableName("ag_agent")
public class Agent extends BasePojo {

    /**
     * 代理商ID
     */
    private Long id;

    /**
     * 代理商名称
     */
    private String agentName;

    /**
     * 代理省份
     */
    private Long provinceId;

    /**
     * 代理城市
     */
    private Long cityId;

    /**
     * 代理等级
     */
    private Integer level;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 联系人名称
     */
    private String contactor;

    /**
     * 身份证正面图片链接
     */
    private String idCardFrontSide;

    /**
     * 身份证反面图片链接
     */
    private String idCardBackSide;

    /**
     * 营业执照图片链接
     */
    private String businessLicense;

    /**
     * 代理合同图片链接
     */
    private String contractLink;

    /**
     * 公司介绍
     */
    private String introduction;

    /**
     * 合作开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 合作结束时间
     */
    private LocalDateTime endTime;

    /**
     * 合作状态
     * <p>
     * {@link AgentStatusEnum}
     */
    private Integer status;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 部门编号
     */
    private Long deptId;

}