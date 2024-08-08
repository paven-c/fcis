package com.paven.module.compliance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.paven.common.enums.CommonStatusEnum;
import com.paven.common.enums.DeleteStatusEnum;
import com.paven.common.enums.FieldTypeEnum;
import com.paven.common.pojo.PageResult;
import com.paven.module.common.api.dict.DictTypeApi;
import com.paven.module.compliance.controller.field.vo.FieldCreateReqVO;
import com.paven.module.compliance.controller.field.vo.FieldPageReqVO;
import com.paven.module.compliance.controller.field.vo.FieldRespVO;
import com.paven.module.compliance.controller.field.vo.FieldUpdateReqVO;
import com.paven.module.compliance.convert.FieldConvert;
import com.paven.module.compliance.repository.dto.FromFieldDTO;
import com.paven.module.compliance.repository.mapper.FieldMapper;
import com.paven.module.compliance.repository.mapper.FormRuleMapper;
import com.paven.module.compliance.repository.pojo.Field;
import com.paven.module.compliance.repository.pojo.FormRule;
import com.paven.module.compliance.service.FieldService;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yanyi
 */
@Service
public class FieldServiceImpl extends ServiceImpl<FieldMapper, Field> implements FieldService {

    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private FormRuleMapper formRuleMapper;
    @Resource
    private DictTypeApi dictTypeApi;

    @Override
    public PageResult<FromFieldDTO> fieldPage(FieldPageReqVO reqVO) {
        return fieldMapper.selectJoinPage(PageResult.build(reqVO), FromFieldDTO.class, new MPJLambdaWrapper<>(Field.class)
                .selectAll(Field.class)
                .select(FormRule::getFormId, FormRule::getFieldId, FormRule::getSort)
                .leftJoin(FormRule.class, condition -> condition.eq(FormRule::getFieldId, Field::getId))
                .eq(Field::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus())
                .eq(Objects.nonNull(reqVO.getFormId()), FormRule::getFormId, reqVO.getFormId())
                .like(StrUtil.isNotBlank(reqVO.getTitle()), Field::getTitle, reqVO.getTitle())
                .eq(Objects.nonNull(reqVO.getType()), Field::getType, reqVO.getType())
                .orderByAsc(FormRule::getSort));
    }

    @Override
    public List<FieldRespVO> fieldList(FieldPageReqVO reqVO) {
        List<Field> fieldList = fieldMapper.selectList(Wrappers.lambdaQuery(Field.class)
                .eq(Field::getFormId, reqVO.getFormId())
                .eq(Field::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus())
                .orderByAsc(Field::getSort));
        return FieldConvert.INSTANCE.convertVoList(fieldList);

    }

    @Override
    public Field detail(Long id) {
        return fieldMapper.selectById(id);
//        return formRuleMapper.selectJoinOne(FieldRespVO.class, new MPJLambdaWrapper<>(FormRule.class)
//                .select(FormRule::getFormId, FormRule::getFieldId, FormRule::getSort)
//                .selectAll(Field.class)
//                .leftJoin(Field.class, condition -> condition
//                        .eq(Field::getId, FormRule::getFieldId)
//                        .eq(Field::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus()))
//                .eq(Field::getId, id)
//                .orderByAsc(FormRule::getSort)
//                .last("LIMIT 1"));
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(Long userId, FieldCreateReqVO reqVO) {
        boolean dictField = FieldTypeEnum.isDictField(reqVO.getType());
        Field field = Field.builder().title(reqVO.getTitle())
                .name(reqVO.getName())
                .type(reqVO.getType())
                .status(Objects.nonNull(reqVO.getStatus()) ? reqVO.getStatus() : CommonStatusEnum.ENABLE.getStatus())
                .dicType(dictField ? reqVO.getName() : null).build();
        fieldMapper.insert(field);
        formRuleMapper.insert(FormRule.builder().formId(reqVO.getFormId())
                .fieldId(field.getId())
                .sort(0).creator(userId)
                .createTime(LocalDateTime.now()).build());
        if (dictField) {
            dictTypeApi.createDictType(field.getTitle(), field.getDicType());
        }
        return field.getId();
    }

    @Override
    public Boolean update(Long userId, FieldUpdateReqVO reqVO) {
        Field field = Field.builder().id(reqVO.getFieldId())
                .title(reqVO.getTitle())
                .name(reqVO.getName())
                .type(reqVO.getType())
                .status(Objects.nonNull(reqVO.getStatus()) ? reqVO.getStatus() : CommonStatusEnum.ENABLE.getStatus())
                .dicType(reqVO.getDicType()).build();
        int count = fieldMapper.updateById(field);
        return count > 0;
    }

    @Override
    public Boolean delete(Long id) {
        Field field = fieldMapper.selectById(id);
        if (Objects.isNull(field)) {
            return true;
        }
        field.setDeleted(DeleteStatusEnum.DELETED.getStatus());
        field.setDeletedTime(LocalDateTime.now());
        int count = fieldMapper.updateById(field);
        return count > 0;
    }


}
