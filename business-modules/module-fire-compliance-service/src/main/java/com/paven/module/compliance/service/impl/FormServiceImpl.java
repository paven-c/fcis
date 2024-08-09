package com.paven.module.compliance.service.impl;

import static com.paven.common.exception.util.ServiceExceptionUtil.exception;
import static com.paven.module.common.enums.ErrorCodeConstants.FORM_NAME_EXISTS;
import static com.paven.module.common.enums.ErrorCodeConstants.FORM_NOT_EXISTS;
import static com.paven.module.common.enums.ErrorCodeConstants.FORM_RULE_FIELD_NOT_EXISTS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.paven.common.enums.ComparisonOperator;
import com.paven.common.enums.DeleteStatusEnum;
import com.paven.common.enums.FieldTypeEnum;
import com.paven.common.pojo.PageResult;
import com.paven.common.util.collection.CollectionUtils;
import com.paven.common.util.json.JsonUtils;
import com.paven.module.compliance.controller.field.vo.FieldPageReqVO;
import com.paven.module.compliance.controller.field.vo.FieldRespVO;
import com.paven.module.compliance.controller.form.vo.FormCheckReqVO;
import com.paven.module.compliance.controller.form.vo.FormCreateReqVO;
import com.paven.module.compliance.controller.form.vo.FormPageReqVO;
import com.paven.module.compliance.controller.form.vo.FormRespVO;
import com.paven.module.compliance.controller.form.vo.FormRuleRespVO;
import com.paven.module.compliance.controller.form.vo.FormRuleSaveReqVO;
import com.paven.module.compliance.controller.form.vo.FormUpdateReqVO;
import com.paven.module.compliance.convert.FormConvert;
import com.paven.module.compliance.repository.dto.FieldConditionDTO;
import com.paven.module.compliance.repository.mapper.FormRuleMapper;
import com.paven.module.compliance.repository.mapper.FormMapper;
import com.paven.module.compliance.repository.pojo.Field;
import com.paven.module.compliance.repository.pojo.Form;
import com.paven.module.compliance.repository.pojo.FormRule;
import com.paven.module.compliance.service.FieldService;
import com.paven.module.compliance.service.FormService;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yanyi
 */
@Service
public class FormServiceImpl extends ServiceImpl<FormMapper, Form> implements FormService {

    @Resource
    private FormMapper formMapper;
    @Resource
    private FormRuleMapper formRuleMapper;
    @Resource
    private FieldService fieldService;

    @Override
    public Long create(FormCreateReqVO reqVO) {
        validateNameExists(null, reqVO.getName());
        Form build = Form.builder().name(reqVO.getName()).status(reqVO.getStatus()).build();
        formMapper.insert(build);
        return build.getId();
    }

    @Override
    public Boolean update(FormUpdateReqVO reqVO) {
        validateNameExists(reqVO.getId(), reqVO.getName());
        int count = formMapper.updateById(Form.builder().id(reqVO.getId()).name(reqVO.getName()).status(reqVO.getStatus()).build());
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        Form build = formMapper.selectById(id);
        if (Objects.isNull(build)) {
            return true;
        }
        // 删除关联关系
        formRuleMapper.delete(Wrappers.lambdaQuery(FormRule.class)
                .eq(FormRule::getFormId, id));
        // 删除建筑
        int count = formMapper.update(Wrappers.lambdaUpdate(Form.class)
                .set(Form::getDeleted, DeleteStatusEnum.DELETED.getStatus())
                .set(Form::getDeletedTime, LocalDateTime.now())
                .eq(Form::getId, id));
        return count > 0;
    }

    @Override
    public PageResult<FormRespVO> getFormPage(FormPageReqVO reqVO) {
        PageResult<Form> pageResult = formMapper.selectPage(reqVO, Wrappers.lambdaQuery(Form.class)
                .eq(Objects.nonNull(reqVO.getFormId()), Form::getId, reqVO.getFormId())
                .like(StrUtil.isNotBlank(reqVO.getName()), Form::getName, reqVO.getName())
                .eq(Form::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus())
                .orderByDesc(Form::getUpdateTime));
        return PageResult.convert(FormConvert.INSTANCE.convertList(pageResult.getList()), pageResult);
    }

    @Override
    public List<FormRespVO> getFormList(FormPageReqVO reqVO) {
        List<Form> buildList = formMapper.selectList(Wrappers.lambdaQuery(Form.class)
                .eq(Objects.nonNull(reqVO.getFormId()), Form::getId, reqVO.getFormId())
                .like(StrUtil.isNotBlank(reqVO.getName()), Form::getName, reqVO.getName())
                .eq(Form::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus())
                .orderByDesc(Form::getUpdateTime));
        return FormConvert.INSTANCE.convertList(buildList);
    }

    @Override
    public FormRespVO getFormDetail(Long id) {
        Form build = formMapper.selectOne(Wrappers.lambdaQuery(Form.class)
                .eq(Form::getId, id)
                .eq(Form::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus())
                .last("LIMIT 1"));
        if (Objects.isNull(build)) {
            return null;
        }
        FormRespVO respVO = FormConvert.INSTANCE.convert(build);
        respVO.setFieldList(fieldService.fieldList(FieldPageReqVO.builder().formId(build.getId()).build()));
        return respVO;
    }

    @Override
    public Boolean updateRule(Long userId, FormRuleSaveReqVO reqVO) {
        FormRespVO formDetail = getFormDetail(reqVO.getFormId());
        if (Objects.isNull(formDetail)) {
            throw exception(FORM_NOT_EXISTS);
        }
        // 整合条件列表数据
        AtomicInteger sort = new AtomicInteger();
        List<FormRule> ruleList = Optional.ofNullable(reqVO.getConditions()).orElse(Lists.newArrayList())
                .stream().flatMap(condition -> {
                    FormRule outerRule = FormRule.builder()
                            .id(condition.getId())
                            .formId(reqVO.getFormId())
                            .fieldId(condition.getFieldId())
                            .operator(condition.getOperator())
                            .value(condition.getValue())
                            .parentId(0L)
                            .sort(sort.getAndIncrement())
                            .creator(condition.getId() != null ? null : userId)
                            .createTime(condition.getId() != null ? null : LocalDateTime.now())
                            .build();
                    if (outerRule.getId() == null) {
                        formRuleMapper.insert(outerRule);
                    }
                    AtomicInteger innerSort = new AtomicInteger();
                    List<FormRule> rules = Optional.ofNullable(condition.getConditions()).orElse(Lists.newArrayList())
                            .stream()
                            .map(innerRule -> FormRule.builder()
                                    .id(innerRule.getId())
                                    .formId(reqVO.getFormId())
                                    .fieldId(innerRule.getFieldId())
                                    .operator(innerRule.getOperator())
                                    .value(innerRule.getValue())
                                    .parentId(outerRule.getId())
                                    .sort(innerSort.getAndIncrement())
                                    .creator(innerRule.getId() != null ? null : userId)
                                    .createTime(innerRule.getId() != null ? null : LocalDateTime.now())
                                    .build()).collect(Collectors.toList());
                    rules.add(outerRule);
                    return rules.stream();
                }).toList();
        // 校验表单字段
        Map<Long, FieldRespVO> fieldMap = Optional.ofNullable(fieldService.fieldList(FieldPageReqVO.builder().formId(reqVO.getFormId()).build()))
                .orElse(Lists.newArrayList()).stream().collect(Collectors.toMap(FieldRespVO::getId, field -> field));
        if (CollUtil.isNotEmpty(reqVO.getConditions())) {
            List<Long> ids = ruleList.stream().map(FormRule::getFieldId).filter(id -> Objects.nonNull(id) && !fieldMap.containsKey(id)).toList();
            if (CollUtil.isNotEmpty(ids)) {
                throw exception(FORM_RULE_FIELD_NOT_EXISTS);
            }
        }
        // 数据库操作
        List<FormRule> dbRuleList = formRuleMapper.selectList(Wrappers.lambdaQuery(FormRule.class).eq(FormRule::getFormId, reqVO.getFormId()));
        List<List<FormRule>> diffRuleList = CollectionUtils.diffList(dbRuleList, ruleList, (dbRule, rule) -> dbRule.getId().equals(rule.getId()));
        if (CollUtil.isNotEmpty(diffRuleList.get(0))) {
            formRuleMapper.insertBatch(diffRuleList.get(0));
        }
        if (CollUtil.isNotEmpty(diffRuleList.get(1))) {
            formRuleMapper.updateBatch(diffRuleList.get(1));
        }
        if (CollUtil.isNotEmpty(diffRuleList.get(2))) {
            formRuleMapper.deleteBatchIds(diffRuleList.get(2).stream().map(FormRule::getId).toList());
        }
        return true;
    }

    @Override
    public Boolean check(FormCheckReqVO reqVO) {
        // 表单数据
        Map<String, Object> data = reqVO.getData();
        // 表单详情
        FormRespVO respVO = Optional.ofNullable(reqVO.getFormId()).map(this::getFormDetail).orElseThrow(() -> exception(FORM_NOT_EXISTS));
        // 表单字段
        Map<String, String> fieldMap = Optional.ofNullable(respVO.getFieldList()).orElse(Lists.newArrayList()).stream()
                .collect(Collectors.toMap(FieldRespVO::getName, FieldRespVO::getType));
        // 条件规则
        List<FieldConditionDTO> conditionList = JsonUtils.parseArray(respVO.getConditions(), FieldConditionDTO.class);
        if (CollUtil.isEmpty(conditionList)) {
            return true;
        }
        return conditionList.stream().allMatch(fieldCondition -> {
            if (CollUtil.isNotEmpty(fieldCondition.getConditions())) {
                return fieldCondition.getConditions().stream()
                        .anyMatch(subCondition -> checkValue(subCondition, FieldTypeEnum.findByCode(fieldMap.get(subCondition.getFieldName())), data));
            } else {
                return checkValue(fieldCondition, FieldTypeEnum.findByCode(fieldMap.get(fieldCondition.getFieldName())), data);
            }
        });
    }

    @Override
    public FormRuleRespVO getRuleList(Long formId) {
        Form form = formMapper.selectById(formId);
        if (Objects.isNull(form)) {
            return null;
        }
        // 字段列表
        Map<Long, FieldRespVO> fieldMap = fieldService.fieldList(FieldPageReqVO.builder().formId(formId).build()).stream()
                .collect(Collectors.toMap(FieldRespVO::getId, field -> field));
        // 规则列表
        List<FormRule> ruleList = formRuleMapper.selectList(Wrappers.lambdaQuery(FormRule.class)
                .eq(FormRule::getFormId, formId)
                .orderByAsc(FormRule::getParentId)
                .orderByAsc(FormRule::getSort));
        Map<Long, List<FormRule>> ruleMap = ruleList.stream().collect(Collectors.groupingBy(FormRule::getParentId));
        FormRuleRespVO respVO = new FormRuleRespVO();
        respVO.setFormId(formId);
        respVO.setConditions(Optional.ofNullable(ruleMap.get(0L)).orElse(Lists.newArrayList())
                .stream()
                .map(rule -> {
                    FieldConditionDTO condition = new FieldConditionDTO();
                    condition.setId(rule.getId());
                    condition.setFieldId(rule.getFieldId());
                    FieldRespVO field = fieldMap.get(rule.getFieldId());
                    if (Objects.nonNull(field)) {
                        condition.setFieldName(field.getName());
                        condition.setFieldTitle(field.getTitle());
                    }
                    condition.setLogicalOperator("AND");
                    condition.setOperator(rule.getOperator());
                    condition.setValue(rule.getValue());
                    condition.setConditions(Optional.ofNullable(ruleMap.get(rule.getId())).orElse(Lists.newArrayList()).stream().map(innerRule -> {
                        FieldConditionDTO innerCondition = new FieldConditionDTO();
                        innerCondition.setId(innerRule.getId());
                        innerCondition.setFieldId(innerRule.getFieldId());
                        FieldRespVO innerField = fieldMap.get(innerRule.getFieldId());
                        if (Objects.nonNull(innerField)) {
                            innerCondition.setFieldName(innerField.getName());
                            innerCondition.setFieldTitle(innerField.getTitle());
                        }
                        innerCondition.setLogicalOperator("OR");
                        innerCondition.setOperator(innerRule.getOperator());
                        innerCondition.setValue(innerRule.getValue());
                        return innerCondition;
                    }).toList());
                    return condition;
                }).toList());
        return respVO;
    }

    public static boolean checkValue(FieldConditionDTO condition, FieldTypeEnum type, Map<String, Object> data) {
        String fieldName = condition.getFieldName();
        Object fieldValue = data.get(fieldName);
        String fieldValueStr = fieldValue != null ? fieldValue.toString() : null;
        String conditionValueStr = condition.getValue() != null ? condition.getValue().toString() : null;
        if (fieldValueStr == null || conditionValueStr == null) {
            return false;
        }
        return switch (type) {
            case TEXT, TEXTAREA, PASSWORD -> checkStringCondition(condition, fieldValueStr, conditionValueStr);
            case DATE, TIME_PICKER, DATE_RANGE, TIME_RANGE -> checkDateCondition(condition, fieldValueStr, conditionValueStr);
            case RADIO, SELECT, CHECKBOX -> checkListCondition(condition, fieldValueStr, conditionValueStr);
            default -> false;
        };
    }

    private static boolean checkStringCondition(FieldConditionDTO condition, String fieldValueStr, String conditionValueStr) {
        ComparisonOperator operator = ComparisonOperator.findByName(condition.getOperator());
        if (Objects.isNull(operator)) {
            return false;
        }
        return switch (operator) {
            case EQUALS -> StrUtil.equals(fieldValueStr, conditionValueStr);
            case NOT_EQUALS -> !StrUtil.equals(fieldValueStr, conditionValueStr);
            case CONTAINS -> StrUtil.contains(fieldValueStr, conditionValueStr);
            default -> false;
        };
    }

    private static boolean checkNumberCondition(FieldConditionDTO condition, String fieldValueStr, String conditionValueStr) {
        try {
            BigDecimal fieldValue = new BigDecimal(fieldValueStr);
            BigDecimal conditionValue = new BigDecimal(conditionValueStr);
            ComparisonOperator operator = ComparisonOperator.findByName(condition.getOperator());
            if (Objects.isNull(operator)) {
                return false;
            }
            return switch (operator) {
                case EQUALS -> fieldValue.compareTo(conditionValue) == 0;
                case NOT_EQUALS -> fieldValue.compareTo(conditionValue) != 0;
                case GREATER_THAN -> fieldValue.compareTo(conditionValue) > 0;
                case GREATER_THAN_OR_EQUALS -> fieldValue.compareTo(conditionValue) >= 0;
                case LESS_THAN -> fieldValue.compareTo(conditionValue) < 0;
                case LESS_THAN_OR_EQUALS -> fieldValue.compareTo(conditionValue) <= 0;
                case CONTAINS -> {
                    List<String> values = Optional.ofNullable(condition.getValue())
                            .map(value -> StrUtil.split(value.toString(), ','))
                            .orElse(List.of());
                    yield values.contains(fieldValueStr);
                }
                default -> false;
            };
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean checkDateCondition(FieldConditionDTO condition, String fieldValueStr, String conditionValueStr) {
        try {
            BigDecimal fieldDateValue = new BigDecimal(DateUtil.parse(fieldValueStr).getTime());
            BigDecimal conditionDateValue = new BigDecimal(DateUtil.parse(conditionValueStr).getTime());
            ComparisonOperator operator = ComparisonOperator.findByName(condition.getOperator());
            if (Objects.isNull(operator)) {
                return false;
            }
            return switch (operator) {
                case EQUALS -> fieldDateValue.compareTo(conditionDateValue) == 0;
                case NOT_EQUALS -> fieldDateValue.compareTo(conditionDateValue) != 0;
                case GREATER_THAN -> fieldDateValue.compareTo(conditionDateValue) > 0;
                case GREATER_THAN_OR_EQUALS -> fieldDateValue.compareTo(conditionDateValue) >= 0;
                case LESS_THAN -> fieldDateValue.compareTo(conditionDateValue) < 0;
                case LESS_THAN_OR_EQUALS -> fieldDateValue.compareTo(conditionDateValue) <= 0;
                default -> false;
            };
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkListCondition(FieldConditionDTO condition, String fieldValueStr, String conditionValueStr) {
        ComparisonOperator operator = ComparisonOperator.findByName(condition.getOperator());
        if (Objects.isNull(operator)) {
            return false;
        }
        return switch (operator) {
            case EQUALS -> StrUtil.equals(fieldValueStr, conditionValueStr);
            case NOT_EQUALS -> !StrUtil.equals(fieldValueStr, conditionValueStr);
            case CONTAINS -> {
                List<String> values = Optional.ofNullable(fieldValueStr)
                        .map(value -> StrUtil.split(value, ','))
                        .orElse(List.of());
                yield values.contains(conditionValueStr);
            }
            default -> false;
        };
    }

    /**
     * 校验建筑名称是否重复
     *
     * @param name 建筑名称
     */
    private void validateNameExists(Long formId, String name) {
        List<Form> builds = formMapper.selectList(Wrappers.lambdaQuery(Form.class)
                .eq(Form::getName, name)
                .eq(Form::getDeleted, DeleteStatusEnum.ACTIVATED.getStatus()));
        // 不存在同名的记录提示不存在
        if (CollUtil.isEmpty(builds)) {
            return;
        }
        // 存在同名记录且无排除的ID提示已存在
        if (Objects.isNull(formId)) {
            throw exception(FORM_NAME_EXISTS);
        }
        // 存在同名记录且与排除的ID相同提示不存在
        if (!builds.stream().allMatch(build -> build.getId().equals(formId))) {
            throw exception(FORM_NAME_EXISTS);
        }
    }
}
