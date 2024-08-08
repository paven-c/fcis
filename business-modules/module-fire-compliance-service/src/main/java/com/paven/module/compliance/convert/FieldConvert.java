package com.paven.module.compliance.convert;


import com.paven.module.compliance.controller.field.vo.FieldRespVO;
import com.paven.module.compliance.repository.dto.FromFieldDTO;
import com.paven.module.compliance.repository.pojo.Field;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface FieldConvert {

    FieldConvert INSTANCE = Mappers.getMapper(FieldConvert.class);

    @Mapping(target = "configJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getConfigJson()))")
    @Mapping(target = "slotJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getSlotJson()))")
    @Mapping(target = "styleJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getStyleJson()))")
    @Mapping(target = "autosizeJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getAutosizeJson()))")
    @Mapping(target = "propsJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getPropsJson()))")
    @Mapping(target = "pickerOptionsJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getPickerOptionsJson()))")
    FieldRespVO convert(FromFieldDTO bean);

    List<FieldRespVO> convertList(List<FromFieldDTO> list);

    @Mapping(target = "configJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getConfigJson()))")
    @Mapping(target = "slotJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getSlotJson()))")
    @Mapping(target = "styleJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getStyleJson()))")
    @Mapping(target = "autosizeJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getAutosizeJson()))")
    @Mapping(target = "propsJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getPropsJson()))")
    @Mapping(target = "pickerOptionsJson", expression = "java(cn.hutool.json.JSONUtil.parseObj(bean.getPickerOptionsJson()))")
    FieldRespVO convertVo(Field bean);

    List<FieldRespVO> convertVoList(List<Field> list);
}
