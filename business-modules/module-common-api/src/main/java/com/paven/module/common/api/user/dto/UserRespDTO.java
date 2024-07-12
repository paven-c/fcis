package com.paven.module.common.api.user.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author paven
 */
@Data
public class UserRespDTO {

    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 帐号状态
     */
    private Integer status;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginDate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
