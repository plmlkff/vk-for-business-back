package ru.itmo.blpslab1.service.interceptor;

import arrow.core.Either
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Aspect
@Component
class ServiceInterceptor{
    @Pointcut("within(ru.itmo.blpslab1.service.*)")
    fun onService(){}

    @Around("onService()")
    fun serviceProceeding(proceedingJoinPoint: ProceedingJoinPoint): Either<HttpStatus, *>{
        val res = proceedingJoinPoint.proceed() as Either<HttpStatus, *>
        return res
    }
}
