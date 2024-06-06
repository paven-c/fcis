package com.fancy.component.security.core.aop;

import static com.fancy.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;
import static com.fancy.common.exception.util.ServiceExceptionUtil.exception;

import com.fancy.component.security.core.annotations.PreAuthenticated;
import com.fancy.component.security.core.util.SecurityFrameworkUtils;
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
