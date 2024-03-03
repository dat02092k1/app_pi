package com.project.shopapp.components.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.logging.Logger;

@Component
@Aspect 
public class UserActivityLogger {
    private final Logger logger = Logger.getLogger(getClass().getName());
    // named pointcut
    @Pointcut("within((@org.springframework.web.bind.annotation.RestController *))")
    public void controllerMethods() {}

    @Around("controllerMethods() && execution(* com.project.shopapp.controllers.UserController.*(..))")
    public Object logUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        // log before method execution
        String methodName = joinPoint.getSignature().getName();
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
        logger.info("User activity: " + remoteAddress + " accessed " + methodName);
        // proceed to the method
        Object result = joinPoint.proceed();
        // log after method execution
        logger.info("User activity: " + remoteAddress + " finished accessing " + methodName);
        return result;
    }
}
