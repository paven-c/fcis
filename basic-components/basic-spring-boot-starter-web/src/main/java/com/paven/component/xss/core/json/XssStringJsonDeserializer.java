package com.paven.component.xss.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.paven.common.util.servlet.ServletUtils;
import com.paven.component.xss.config.XssProperties;
import com.paven.component.xss.core.clean.XssCleaner;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PathMatcher;

/**
 * XSS 过滤 jackson 反序列化器。 在反序列化的过程中，会对字符串进行 XSS 过滤。
 *
 * @author paven
 */
@Slf4j
@AllArgsConstructor
public class XssStringJsonDeserializer extends StringDeserializer {

    /**
     * 属性
     */
    private final XssProperties properties;
    /**
     * 路径匹配器
     */
    private final PathMatcher pathMatcher;

    private final XssCleaner xssCleaner;

    @Override
    public String deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        // 1. 白名单 URL 的处理
        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null) {
            String uri = ServletUtils.getRequest().getRequestURI();
            if (properties.getExcludeUrls().stream().anyMatch(excludeUrl -> pathMatcher.match(excludeUrl, uri))) {
                return p.getText();
            }
        }

        // 2. 真正使用 xssCleaner 进行过滤
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            return xssCleaner.clean(p.getText());
        }
        JsonToken t = p.currentToken();
        // [databind#381]
        if (t == JsonToken.START_ARRAY) {
            return _deserializeFromArray(p, deserializationContext);
        }
        // need to gracefully handle byte[] data, as base64
        if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
            Object ob = p.getEmbeddedObject();
            if (ob == null) {
                return null;
            }
            if (ob instanceof byte[]) {
                return deserializationContext.getBase64Variant().encode((byte[]) ob, false);
            }
            // otherwise, try conversion using toString()...
            return ob.toString();
        }
        // 29-Jun-2020, tatu: New! "Scalar from Object" (mostly for XML)
        if (t == JsonToken.START_OBJECT) {
            return deserializationContext.extractScalarFromObject(p, this, _valueClass);
        }

        if (t.isScalarValue()) {
            String text = p.getValueAsString();
            return xssCleaner.clean(text);
        }
        return (String) deserializationContext.handleUnexpectedToken(_valueClass, p);
    }
}

