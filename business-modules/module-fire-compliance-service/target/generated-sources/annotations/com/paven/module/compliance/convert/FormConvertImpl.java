package com.paven.module.compliance.convert;

import com.paven.module.compliance.controller.form.vo.FormRespVO;
import com.paven.module.compliance.repository.pojo.Form;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-05T16:39:59+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
public class FormConvertImpl implements FormConvert {

    @Override
    public FormRespVO convert(Form bean) {
        if ( bean == null ) {
            return null;
        }

        FormRespVO.FormRespVOBuilder formRespVO = FormRespVO.builder();

        formRespVO.id( bean.getId() );
        formRespVO.name( bean.getName() );
        formRespVO.status( bean.getStatus() );
        formRespVO.conditions( bean.getConditions() );
        formRespVO.createTime( bean.getCreateTime() );
        formRespVO.updateTime( bean.getUpdateTime() );
        formRespVO.creator( bean.getCreator() );
        formRespVO.updater( bean.getUpdater() );

        return formRespVO.build();
    }
}
