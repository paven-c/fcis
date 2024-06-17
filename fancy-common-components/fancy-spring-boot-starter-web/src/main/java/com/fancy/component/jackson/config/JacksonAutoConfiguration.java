package com.fancy.component.jackson.config;

import cn.hutool.core.collection.CollUtil;
import com.fancy.common.util.date.DateUtils;
import com.fancy.common.util.json.JsonUtils;
import com.fancy.component.jackson.core.databind.NumberSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author paven
 */
@AutoConfiguration
@Slf4j
public class JacksonAutoConfiguration {

    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public JsonUtils jsonUtils(List<ObjectMapper> objectMappers) {
        // 1.1 创建 SimpleModule 对象
        SimpleModule simpleModule = new SimpleModule();
        simpleModule
                // 新增 Long 类型序列化规则，数值超过 2^53-1，在 JS 会出现精度丢失问题，因此 Long 自动序列化为字符串类型
                .addSerializer(Long.class, NumberSerializer.INSTANCE)
                .addSerializer(Long.TYPE, NumberSerializer.INSTANCE)
                .addSerializer(
                        LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)))
                .addDeserializer(
                        LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)));
//                .addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE)
//                .addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE)
//                .addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE)
//                .addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE)
//                // 新增 LocalDateTime 序列化、反序列化规则，使用 Long 时间戳
//                .addSerializer(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE)
//                .addDeserializer(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
        objectMappers.forEach(objectMapper -> objectMapper.registerModule(simpleModule));
        JsonUtils.init(CollUtil.getFirst(objectMappers));
        log.info("[init][初始化 JsonUtils 成功]");
        return new JsonUtils();
    }

}
