package com.fancy.module.common.repository.pojo.permission;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fancy.common.enums.CommonStatusEnum;
import com.fancy.component.mybatis.core.dataobject.BasePojo;
import com.fancy.module.common.enums.permission.MenuTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单
 *
 * @author paven
 */
@TableName("menu")
@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends BasePojo {

    /**
     * 菜单编号 - 根节点
     */
    public static final Long ID_ROOT = 0L;

    /**
     * 菜单编号
     */
    @TableId
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 权限标识 一般格式为：
     * <p>
     * ${系统}:${模块}:${操作} 例如说：system:admin:add，即 system 服务的添加管理员。 当我们把该 Menu 赋予给角色后，意味着该角色有该资源： - 对于后端，配合 @PreAuthorize 注解，配置 API 接口需要该权限，从而对 API 接口进行权限控制。 -
     */
    private String permission;

    /**
     * 菜单类型 枚举 {@link MenuTypeEnum}
     */
    private Integer type;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 路由地址
     * <p>
     * 如果 path 为 http(s) 时，则它是外链
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 组件名
     */
    private String componentName;

    /**
     * 状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 是否可见
     * <p>
     * 只有菜单、目录使用 当设置为 true 时，该菜单不会展示在侧边栏，但是路由还是存在。例如说，一些独立的编辑页面 /edit/1024 等等
     */
    private Boolean visible;

    /**
     * 是否缓存
     * <p>
     * 只有菜单、目录使用，否使用 Vue 路由的 keep-alive 特性 注意：如果开启缓存，则必须填写 {@link #componentName} 属性，否则无法缓存
     */
    private Boolean keepAlive;

}
