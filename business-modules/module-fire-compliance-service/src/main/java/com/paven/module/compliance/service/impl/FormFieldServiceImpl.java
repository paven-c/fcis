package com.paven.module.compliance.service.impl;

import static cn.hutool.json.JSONUtil.toJsonStr;
import static com.paven.common.exception.util.ServiceExceptionUtil.exception;
import static com.paven.common.util.collection.CollectionUtils.convertSet;
import static com.paven.module.common.enums.ErrorCodeConstants.FORM_FIELD_NAME_DUPLICATE;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.paven.common.util.collection.CollectionUtils;
import com.paven.common.util.json.JsonUtils;
import com.paven.module.common.api.dict.DictDataApi;
import com.paven.module.common.api.dict.dto.DictDataDTO;
import com.paven.module.compliance.controller.form.vo.FormFieldCreateReqVO;
import com.paven.module.compliance.controller.form.vo.FormFieldSaveReqVO;
import com.paven.module.compliance.repository.mapper.FieldMapper;
import com.paven.module.compliance.repository.mapper.FormRuleMapper;
import com.paven.module.compliance.repository.pojo.Field;
import com.paven.module.compliance.repository.pojo.FormRule;
import com.paven.module.compliance.service.FormFieldService;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yanyi
 */
@Service
public class FormFieldServiceImpl extends ServiceImpl<FormRuleMapper, FormRule> implements FormFieldService {

    @Resource
    private FormRuleMapper formRuleMapper;
    @Resource
    private FieldMapper fieldMapper;
    @Resource
    private DictDataApi dictDataApi;

    @Override
    public Boolean create(Long userId, FormFieldCreateReqVO reqVO) {
        FormRule buildField = FormRule.builder()
                .formId(reqVO.getFormId()).fieldId(reqVO.getId())
                .sort(Objects.isNull(reqVO.getSort()) ? 0 : reqVO.getSort())
                .creator(userId).createTime(LocalDateTime.now()).build();
        int count = formRuleMapper.insert(buildField);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(Long userId, FormFieldSaveReqVO reqVO) {
        AtomicInteger fieldSort = new AtomicInteger();
        // 参数字段
        List<Field> fieldList = Optional.ofNullable(reqVO.getFields()).orElse(Lists.newArrayList())
                .stream().map(field -> {
                    AtomicInteger sort = new AtomicInteger();
                    // 字段属性
                    Optional<JSONObject> configJson = Optional.ofNullable(field.getConfigJson());
                    String fieldTitle = configJson.map(config -> config.getStr("label")).orElse("");
                    String fieldType = configJson.map(config -> config.getStr("tagIcon")).orElse("");
                    String fieldName = field.getName();
                    // 字段属性
                    List<DictDataDTO> fieldDictDataList = Optional.ofNullable(field.getSlotJson())
                            .map(slogJson -> JsonUtils.parseArray(JsonUtils.toJsonString(slogJson.getJSONArray("options")), DictDataDTO.class))
                            .orElse(Lists.newArrayList()).stream().peek(dictData -> {
                                dictData.setDictType(fieldName);
                                dictData.setSort(sort.getAndIncrement());
                            }).toList();
                    return Field.builder().id(field.getId()).formId(reqVO.getFormId())
                            .title(fieldTitle).name(fieldName).type(fieldType).dicType(fieldName)
                            .configJson(toJsonStr(field.getConfigJson())).slotJson(toJsonStr(field.getSlotJson()))
                            .styleJson(toJsonStr(field.getStyleJson()))
                            .autosizeJson(toJsonStr(field.getAutosizeJson())).propsJson(toJsonStr(field.getPropsJson()))
                            .showPassword(field.getShowPassword()).placeholder(field.getPlaceholder()).clearable(field.getClearable())
                            .prefixIcon(field.getPrefixIcon()).suffixIcon(field.getSuffixIcon())
                            .maxLength(field.getMaxLength()).size(field.getSize()).showWordLimit(field.getShowWordLimit())
                            .showAllLevels(field.getShowAllLevels())
                            .readonly(field.getReadonly()).disabled(field.getDisabled()).filterable(field.getFilterable()).multiple(field.getMultiple())
                            .separator(field.getSeparator()).activeText(field.getActiveText()).inactiveText(field.getInactiveText())
                            .activeColor(field.getActiveColor()).inactiveColor(field.getInactiveColor())
                            .activeValue(field.getActiveValue()).inactiveValue(field.getInactiveValue())
                            .isRange(field.getIsRange()).rangeSeparator(field.getRangeSeparator())
                            .startPlaceholder(field.getStartPlaceholder()).endPlaceholder(field.getEndPlaceholder())
                            .pickerOptionsJson(toJsonStr(field.getPickerOptionsJson())).format(field.getFormat()).valueFormat(field.getValueFormat())
                            .status(field.getStatus()).sort(fieldSort.getAndIncrement()).dictDataList(fieldDictDataList).build();
                }).toList();
        if (CollUtil.isEmpty(fieldList)) {
            Map<String, List<Field>> fieldNameMap = fieldList.stream().collect(Collectors.groupingBy(Field::getName));
            List<String> fieldNames = fieldNameMap.values().stream().filter(list -> list.size() > 1).map(list -> list.get(0).getName()).toList();
            if (CollUtil.isNotEmpty(fieldNames)) {
                throw exception(FORM_FIELD_NAME_DUPLICATE, StrUtil.join(StrUtil.COMMA, fieldNames));
            }
        }
        // 字段数据字典列表
        List<DictDataDTO> dictDataList = Lists.newArrayList();
        // 数据库字段
        List<Field> dbFieldList = fieldMapper.selectList(Wrappers.lambdaQuery(Field.class).eq(Field::getFormId, reqVO.getFormId()));
        // 比较字段
        List<List<Field>> diffFieldList = CollectionUtils.diffList(dbFieldList, fieldList, (oldField, newField) -> oldField.getId().equals(newField.getId()));
        List<Field> insertFieldList = diffFieldList.get(0);
        if (CollUtil.isNotEmpty(insertFieldList)) {
            fieldMapper.insertBatch(insertFieldList);
            List<DictDataDTO> dictDataDTOList = insertFieldList.stream()
                    .flatMap(field -> field.getDictDataList().stream().peek(dicData -> dicData.setFieldId(field.getId())))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(dictDataDTOList)) {
                dictDataList.addAll(dictDataDTOList);
            }
        }
        List<Field> updateFieldList = diffFieldList.get(1);
        if (CollUtil.isNotEmpty(updateFieldList)) {
            fieldMapper.updateBatch(updateFieldList);
            List<DictDataDTO> dictDataDTOList = updateFieldList.stream()
                    .flatMap(field -> field.getDictDataList().stream().peek(dicData -> dicData.setFieldId(field.getId())))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(dictDataDTOList)) {
                dictDataList.addAll(dictDataDTOList);
            }
        }
        if (CollUtil.isNotEmpty(diffFieldList.get(2))) {
            fieldMapper.deleteBatchIds(convertSet(diffFieldList.get(2), Field::getId));
        }
        // 数据库数据字典
        List<DictDataDTO> dbDictDataList = dictDataApi.getFieldDictDataList(fieldList.stream().map(Field::getId).toList());
        List<List<DictDataDTO>> diffDicDataList = CollectionUtils.diffList(dbDictDataList, dictDataList, (oldDictData, newDictData) ->
                oldDictData.getFieldId().equals(newDictData.getFieldId()) && oldDictData.getValue().equals(newDictData.getValue()));
        List<DictDataDTO> insertDictDataList = diffDicDataList.get(0);
        if (CollUtil.isNotEmpty(insertDictDataList)) {
            dictDataApi.insertBatch(insertDictDataList);
        }
        List<DictDataDTO> updateDictDataList = diffDicDataList.get(1);
        if (CollUtil.isNotEmpty(updateDictDataList)) {
            dictDataApi.updateBatch(updateDictDataList);
        }
        if (CollUtil.isNotEmpty(diffDicDataList.get(2))) {
            dictDataApi.deleteBatchIds(convertSet(diffDicDataList.get(2), DictDataDTO::getId));
        }
        return true;
    }
}
