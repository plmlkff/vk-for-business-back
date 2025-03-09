package ru.itmo.blpslab1.service.interceptor;

import arrow.core.Either
import arrow.core.left
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.itmo.blpslab1.service.exceptions.RollbackTransactionException

@Aspect
@Component
class ServiceInterceptor{
    val logger: Logger = LoggerFactory.getLogger(ServiceInterceptor::class.java)

    @Pointcut("within(ru.itmo.blpslab1.service..*)")
    fun onService(){}

    @Around("onService()")
    fun serviceProceeding(proceedingJoinPoint: ProceedingJoinPoint): Either<HttpStatus, *>{
        try {
            logger.debug("Invoked method: {} with args: {}", proceedingJoinPoint.signature.name, proceedingJoinPoint.args)
            val res = proceedingJoinPoint.proceed() as Either<HttpStatus, *>
            return res
        } catch (e: Throwable) {
            return when (e) {
                is RollbackTransactionException -> e.status.left()

                else -> return HttpStatus.INTERNAL_SERVER_ERROR.left()
            }
        }
    }
}
