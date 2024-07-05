package com.paven.module.compliance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.paven.common.pojo.PageResult;
import com.paven.module.compliance.controller.field.vo.FieldCreateReqVO;
import com.paven.module.compliance.controller.field.vo.FieldPageReqVO;
import com.paven.module.compliance.controller.field.vo.FieldRespVO;
import com.paven.module.compliance.controller.field.vo.FieldUpdateReqVO;
import com.paven.module.compliance.repository.pojo.Field;
import java.util.List;

/**
 * @author Yanyi
 */
public interface FieldService extends IService<Field> {

    /**
     * 字段分页列表
     *
     * @param reqVO 分页条件
     * @return 字段分页列表
     */
    PageResult<FieldRespVO> fieldPage(FieldPageReqVO reqVO);

    /**
     * 字段列表
     *
     * @param reqVO 分页条件
     * @return 字段列表
     */
    List<FieldRespVO> fieldList(FieldPageReqVO reqVO);

    /**
     * 字段详情
     *
     * @param id 字段ID
     * @return 字段详情
     */
    FieldRespVO detail(Long id);

    /**
     * 新增字段
     *
     * @param userId 用户ID
     * @param reqVO  字段参数
     * @return 字段ID
     */
    Long create(Long userId, FieldCreateReqVO reqVO);

    /**
     * 更新字段
     *
     * @param reqVO 字段参数
     * @return 是否更新成功
     */
    Boolean update(Long userId, FieldUpdateReqVO reqVO);

    /**
     * 删除字段
     *
     * @param id 字段ID
     * @return 是否删除成功
     */
    Boolean delete(Long id);

}
