package com.paven.module.compliance.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paven.component.mybatis.core.mapper.BaseMapperX;
import com.paven.module.compliance.controller.field.vo.FieldPageReqVO;
import com.paven.module.compliance.controller.field.vo.FieldRespVO;
import com.paven.module.compliance.repository.pojo.FormRule;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Yanyi
 */
@Mapper
public interface FormRuleMapper extends BaseMapperX<FormRule> {

    /**
     * 获取字段列表
     *
     * @param reqVO 请求参数
     * @return 字段列表
     */
    @Select("SELECT f.*, bf.sort FROM sys_form_rule bf INNER JOIN sys_field f ON bf.field_id = f.id WHERE bf.form_id = #{formId} ORDER BY bf.sort DESC")
    List<FieldRespVO> formFieldList(FieldPageReqVO reqVO);
}
