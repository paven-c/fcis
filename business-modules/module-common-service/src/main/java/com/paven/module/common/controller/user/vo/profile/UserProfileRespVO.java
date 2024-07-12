package com.paven.module.common.controller.user.vo.profile;

import com.paven.module.common.controller.dept.vo.DeptSimpleRespVO;
import com.paven.module.common.controller.permission.vo.role.RoleSimpleRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "用户个人中心信息ResponseVO")
public class UserProfileRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String username;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String nickname;

    @Schema(description = "用户邮箱", example = "")
    private String email;

    @Schema(description = "手机号码", example = "")
    private String mobile;

    @Schema(description = "用户性别，参见 GenderEnum 枚举类", example = "1")
    private Integer gender;

    @Schema(description = "用户头像", example = "")
    private String avatar;

    @Schema(description = "最后登录 IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "192.168.1.1")
    private String loginIp;

    @Schema(description = "最后登录时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime loginDate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;

    /**
     * 所属角色
     */
    private List<RoleSimpleRespVO> roles;
    /**
     * 所在部门
     */
    private DeptSimpleRespVO dept;

}
