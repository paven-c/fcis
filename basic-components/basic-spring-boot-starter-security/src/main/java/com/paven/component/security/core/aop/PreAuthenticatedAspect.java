package com.paven.component.security.core.aop;

import static com.paven.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;
import static com.paven.common.exception.util.ServiceExceptionUtil.exception;

import com.paven.component.security.core.annotations.PreAuthenticated;
import com.paven.component.security.core.util.SecurityFrameworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author paven
 */
@Aspect
@Slf4j
public class PreAuthenticatedAspect {

    @Around("@annotation(preAuthenticated)")
    public Object around(ProceedingJoinPoint joinPoint, PreAuthenticated preAuthenticated) throws Throwable {
        if (SecurityFrameworkUtils.getLoginUser() == null) {
            throw exception(UNAUTHORIZED);
        }
        return joinPoint.proceed();
    }

}
