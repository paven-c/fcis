package com.fancy.common.enums;

/**
 * Web 过滤器顺序的枚举类，保证过滤器按照符合我们的预期
 *
 * @author paven
 */
public interface WebFilterOrderEnum {

    int CORS_FILTER = Integer.MIN_VALUE;

    int TRACE_FILTER = CORS_FILTER + 1;

    int REQUEST_BODY_CACHE_FILTER = Integer.MIN_VALUE + 500;

    // 需要保证在 RequestBodyCacheFilter 后面
    int API_ACCESS_LOG_FILTER = -103;

    // 需要保证在 RequestBodyCacheFilter 后面
    int XSS_FILTER = -102;
}
