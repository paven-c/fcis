package com.paven.module.compliance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.paven.common.pojo.PageResult;
import com.paven.module.compliance.controller.form.vo.FormCheckReqVO;
import com.paven.module.compliance.controller.form.vo.FormCreateReqVO;
import com.paven.module.compliance.controller.form.vo.FormPageReqVO;
import com.paven.module.compliance.controller.form.vo.FormRespVO;
import com.paven.module.compliance.controller.form.vo.FormRuleRespVO;
import com.paven.module.compliance.controller.form.vo.FormRuleSaveReqVO;
import com.paven.module.compliance.controller.form.vo.FormUpdateReqVO;
import com.paven.module.compliance.repository.pojo.Form;
import java.util.List;

/**
 * @author Yanyi
 */
public interface FormService extends IService<Form> {

    /**
     * 新增建筑
     *
     * @param reqVO 参数
     * @return 建筑id
     */
    Long create(FormCreateReqVO reqVO);

    /**
     * 更新建筑
     *
     * @param reqVO 参数
     * @return 是否成功
     */
    Boolean update(FormUpdateReqVO reqVO);

    /**
     * 删除建筑
     *
     * @param id 建筑id
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 获取建筑分页
     *
     * @param reqVO 参数
     * @return 建筑分页
     */
    PageResult<FormRespVO> getFormPage(FormPageReqVO reqVO);

    /**
     * 获取建筑列表
     *
     * @param reqVO 参数
     * @return 建筑列表
     */
    List<FormRespVO> getFormList(FormPageReqVO reqVO);

    /**
     * 获取建筑详情
     *
     * @param id 建筑id
     * @return 建筑详情
     */
    FormRespVO getFormDetail(Long id);

    /**
     * 更新建筑规则
     *
     * @param reqVO 参数
     * @return 是否成功
     */
    Boolean updateRule(Long userId, FormRuleSaveReqVO reqVO);

    /**
     * 检测建筑
     *
     * @param reqVO 参数
     * @return 是否成功
     */
    Boolean check(FormCheckReqVO reqVO);

    /**
     * 获取规则列表
     *
     * @param formId 表单ID
     * @return 规则列表
     */
    FormRuleRespVO getRuleList(Long formId);
}
