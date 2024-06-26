package com.fancy.component.jackson.core.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * 基于时间戳的 Long 反序列化器
 *
 * @author paven
 */
public class TimestampLongDeserializer extends JsonDeserializer<Timestamp> {

    public static final TimestampLongDeserializer INSTANCE = new TimestampLongDeserializer();

    @Override
    public Timestamp deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        // 将 Long 时间戳，转换为 时间戳 对象
        return new Timestamp(p.getValueAsLong());
    }

}
