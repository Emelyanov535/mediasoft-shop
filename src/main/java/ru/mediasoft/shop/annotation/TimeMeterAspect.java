package ru.mediasoft.shop.annotation;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeMeterAspect {

    @Around("@annotation(TimeMeter)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.out.println("TimeMeter::" + joinPoint.getSignature().getDeclaringTypeName() + " executed in " + executionTime + "ms");

        return proceed;
    }
}
