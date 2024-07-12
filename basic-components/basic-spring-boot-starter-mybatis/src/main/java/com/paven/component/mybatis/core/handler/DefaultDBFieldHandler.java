package com.paven.component.mybatis.core.handler;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.paven.common.enums.DeleteStatusEnum;
import com.paven.component.mybatis.core.dataobject.BasePojo;
import com.paven.component.web.core.util.WebFrameworkUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 通用参数填充实现类 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author paven
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BasePojo) {
            BasePojo basePojo = (BasePojo) metaObject.getOriginalObject();

            LocalDateTime current = LocalDateTime.now();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(basePojo.getCreateTime())) {
                basePojo.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(basePojo.getUpdateTime())) {
                basePojo.setUpdateTime(current);
            }

            Long userId = WebFrameworkUtils.getLoginUserId();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if (Objects.nonNull(userId) && Objects.isNull(basePojo.getCreator())) {
                basePojo.setCreator(userId.toString());
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if (Objects.nonNull(userId) && Objects.isNull(basePojo.getUpdater())) {
                basePojo.setUpdater(userId.toString());
            }

            if (Objects.isNull(basePojo.getDeleted())) {
                basePojo.setDeleted(DeleteStatusEnum.ACTIVATED.getStatus());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("updater", metaObject);
        Long userId = WebFrameworkUtils.getLoginUserId();
        if (Objects.nonNull(userId) && Objects.isNull(modifier)) {
            setFieldValByName("updater", userId.toString(), metaObject);
        }
    }
}
