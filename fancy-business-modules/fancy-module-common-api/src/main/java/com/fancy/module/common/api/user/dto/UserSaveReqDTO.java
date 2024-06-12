package com.fancy.module.common.api.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author paven
 */
@Data
@Builder
public class UserSaveReqDTO {

    private String id;

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
     * 部门编号
     */
    private Long deptId;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户性别
     * <p>
     * {@link com.fancy.common.enums.GenderEnum}
     */
    private Integer gender;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     * <p>
     * {@link com.fancy.common.enums.CommonStatusEnum}
     */
    private Integer status;
}