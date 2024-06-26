package com.fancy.component.jackson.core.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * 基于时间戳的 Long 序列化器
 *
 * @author paven
 */
public class TimestampLongSerializer extends JsonSerializer<Timestamp> {

    public static final TimestampLongSerializer INSTANCE = new TimestampLongSerializer();

    @Override
    public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 将 Timestamp 对象，转换为 Long 时间戳
        gen.writeNumber(value.getTime());
    }

}
