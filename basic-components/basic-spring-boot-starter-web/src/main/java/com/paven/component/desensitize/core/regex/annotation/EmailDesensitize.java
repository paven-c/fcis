package com.paven.component.desensitize.core.regex.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.paven.component.desensitize.core.base.annotation.DesensitizeBy;
import com.paven.component.desensitize.core.regex.handler.EmailDesensitizationHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 邮箱脱敏注解
 *
 * @author paven
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = EmailDesensitizationHandler.class)
public @interface EmailDesensitize {

    /**
     * 匹配的正则表达式
     */
    String regex() default "(^.)[^@]*(@.*$)";

    /**
     * 替换规则，邮箱; 比如：example@gmail.com 脱敏之后为 e****@gmail.com
     */
    String replacer() default "$1****$2";
}
