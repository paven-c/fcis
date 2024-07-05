package com.paven.module.compliance.service.impl;

import static com.paven.common.util.collection.CollectionUtils.convertSet;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.paven.common.util.collection.CollectionUtils;
import com.paven.common.util.json.JsonUtils;
import com.paven.module.compliance.controller.form.vo.FormFieldCreateReqVO;
import com.paven.module.compliance.controller.form.vo.FormFieldSaveReqVO;
import com.paven.module.compliance.repository.mapper.FormFieldMapper;
import com.paven.module.compliance.repository.pojo.FormField;
import com.paven.module.compliance.service.FormFieldService;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yanyi
 */
@Service
public class FormFieldServiceImpl extends ServiceImpl<FormFieldMapper, FormField> implements FormFieldService {

    @Resource
    private FormFieldMapper formFieldMapper;

    @Override
    public Boolean create(Long userId, FormFieldCreateReqVO reqVO) {
        FormField buildField = FormField.builder()
                .formId(reqVO.getFormId()).fieldId(reqVO.getFieldId())
                .sort(Objects.isNull(reqVO.getSort()) ? 0 : reqVO.getSort())
                .creator(userId).createTime(LocalDateTime.now()).build();
        int count = formFieldMapper.insert(buildField);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(Long userId, FormFieldSaveReqVO reqVO) {
        LocalDateTime now = LocalDateTime.now();
        // 参数字段
        List<FormField> fieldList = Optional.ofNullable(reqVO.getFields()).orElse(Lists.newArrayList()).stream().map(field -> FormField.builder()
                .formId(field.getFormId()).fieldId(field.getFieldId())
                .sort(Objects.isNull(field.getSort()) ? 0 : field.getSort())
                .creator(userId).createTime(now).build()).toList();
        // 数据库字段
        List<FormField> dbFieldList = formFieldMapper.selectList(Wrappers.lambdaQuery(FormField.class).eq(FormField::getFormId, reqVO.getFormId()));
        // 比较字段
        List<List<FormField>> diffList = CollectionUtils.diffList(
                dbFieldList, fieldList, (oldField, newField) -> oldField.getFieldId().equals(newField.getFieldId()));
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            formFieldMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            formFieldMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            formFieldMapper.deleteBatchIds(convertSet(diffList.get(2), FormField::getId));
        }
        return true;
    }
}
