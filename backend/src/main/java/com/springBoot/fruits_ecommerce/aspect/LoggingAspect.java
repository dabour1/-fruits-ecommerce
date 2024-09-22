package com.springBoot.fruits_ecommerce.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.springBoot.fruits_ecommerce.services.*.*(..))")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
    }

    @Around("execution(* com.springBoot.fruits_ecommerce.services.*.*(..))")
    public Object logPerformanceMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            logger.info("Method {} executed in {} ms", joinPoint.getSignature().toShortString(), duration);
        }
    }

    @AfterThrowing(pointcut = "execution(* com.springBoot.fruits_ecommerce.services.*.*(..))", throwing = "exception")
    public void logExceptions(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {} with arguments: {}",
                joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()), exception);
    }
}
