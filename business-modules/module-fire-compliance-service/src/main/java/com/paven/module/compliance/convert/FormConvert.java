package com.paven.module.compliance.convert;


import com.paven.common.util.collection.CollectionUtils;
import com.paven.module.compliance.controller.form.vo.FormRespVO;
import com.paven.module.compliance.repository.pojo.Form;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author paven
 */
@Mapper
public interface FormConvert {

    FormConvert INSTANCE = Mappers.getMapper(FormConvert.class);

    /**
     * 转换
     *
     * @param bean
     * @return
     */
    FormRespVO convert(Form bean);

    default List<FormRespVO> convertList(List<Form> list) {
        return CollectionUtils.convertList(list, this::convert);
    }
}
