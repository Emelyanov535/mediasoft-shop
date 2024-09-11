package ru.mediasoft.shop.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
public class TransactionalTimeMeterAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                long executionTime = System.currentTimeMillis() - start;
                System.out.println("TransactionalTimeMeter::" + joinPoint.getSignature().getDeclaringTypeName() + " executed in " + executionTime + "ms");
            }
        });
        return proceed;
    }
}
