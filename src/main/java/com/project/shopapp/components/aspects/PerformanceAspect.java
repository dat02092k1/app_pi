package com.project.shopapp.components.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.concurrent.*;
import java.util.logging.Logger;

@Aspect
@Component
public class PerformanceAspect {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

//    @Pointcut("within(com.project.shopapp.controllers.*
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        String methodName = getMethodName(joinPoint);
        logger.info("Starting execution of " + methodName);
    }

    @After("controllerMethods()")
    public void afterMethodExecution(JoinPoint joinPoint) {
        String methodName = getMethodName(joinPoint);
        logger.info("Finished execution of " + methodName);
    }

    @Around("controllerMethods()")
    public Object measureControllerMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object result = joinPoint.proceed();
        long end = System.nanoTime();
        String methodName = getMethodName(joinPoint);
        logger.info("Execution of " + methodName +
                " took " + TimeUnit.NANOSECONDS.toMillis(end - start) + "ms");
        return result;
    }
}
