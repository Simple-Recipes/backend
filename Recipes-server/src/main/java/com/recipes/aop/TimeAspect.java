package com.recipes.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class TimeAspect {
    @Around("execution(* com.recipes.service.*.*(..))")
    public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
        //1. recording time
        long start = System.currentTimeMillis();
        //2. executing the method
        Object object = joinPoint.proceed();
        //3. recording time
        long end = System.currentTimeMillis();
        log.info("Time taken by " + joinPoint.getSignature().getName() + " is " + (end - start) + "ms");

        return object;

    }
}
