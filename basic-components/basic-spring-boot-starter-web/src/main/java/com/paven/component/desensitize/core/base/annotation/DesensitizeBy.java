package com.paven.component.desensitize.core.base.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.paven.component.desensitize.core.base.handler.DesensitizationHandler;
import com.paven.component.desensitize.core.base.serializer.StringDesensitizeSerializer;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 顶级脱敏注解，自定义注解需要使用此注解
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = StringDesensitizeSerializer.class)
public @interface DesensitizeBy {

    /**
     * 脱敏处理器
     */
    @SuppressWarnings("rawtypes")
    Class<? extends DesensitizationHandler> handler();

}
