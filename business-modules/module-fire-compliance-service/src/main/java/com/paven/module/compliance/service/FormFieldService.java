package com.paven.module.compliance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.paven.module.compliance.controller.form.vo.FormFieldCreateReqVO;
import com.paven.module.compliance.controller.form.vo.FormFieldSaveReqVO;
import com.paven.module.compliance.repository.pojo.FormRule;

/**
 * @author Yanyi
 */
public interface FormFieldService extends IService<FormRule> {

    /**
     * 创建建筑字段
     *
     * @param userId 用户ID
     * @param reqVO  创建建筑字段请求参数
     * @return 创建结果
     */
    Boolean create(Long userId, FormFieldCreateReqVO reqVO);

    /**
     * 保存建筑字段
     *
     * @param loginUserId 用户ID
     * @param reqVO       保存建筑字段请求参数
     * @return 保存结果
     */
    Boolean save(Long loginUserId, FormFieldSaveReqVO reqVO);
}
