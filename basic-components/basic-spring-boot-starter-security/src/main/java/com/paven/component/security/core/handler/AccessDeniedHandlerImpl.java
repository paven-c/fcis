package com.paven.component.security.core.handler;

import static com.paven.common.exception.enums.GlobalErrorCodeConstants.FORBIDDEN;

import com.paven.common.exception.enums.GlobalErrorCodeConstants;
import com.paven.common.pojo.CommonResult;
import com.paven.common.util.servlet.ServletUtils;
import com.paven.component.security.core.util.SecurityFrameworkUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 访问一个需要认证的 URL 资源，已经认证（登录）但是没有权限的情况下，返回 {@link GlobalErrorCodeConstants#FORBIDDEN} 错误码。
 *
 * @author paven
 */
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        log.warn("[commence][访问 URL({}) 时，用户({}) 权限不够]", request.getRequestURI(), SecurityFrameworkUtils.getLoginUserId(), e);
        // 返回 403
        ServletUtils.writeJson(response, CommonResult.error(FORBIDDEN));
    }

}
