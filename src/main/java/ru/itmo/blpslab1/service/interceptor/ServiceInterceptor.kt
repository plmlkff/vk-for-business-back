package ru.itmo.blpslab1.service.interceptor;

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import ru.itmo.blpslab1.service.exceptions.RollbackTransactionException
import ru.itmo.blpslab1.utils.service.*

@Aspect
@Component
@Order(1)
class ServiceInterceptor(
    val eventPublisher: ApplicationEventPublisher
){
    val logger: Logger = LoggerFactory.getLogger(ServiceInterceptor::class.java)

    @Pointcut("within(ru.itmo.blpslab1.service.impl.*)")
    fun onService(){}

    @Around("onService()")
    fun serviceProceeding(proceedingJoinPoint: ProceedingJoinPoint): Result<*> {
        try {
            logger.debug("Invoked method: {} with args: {}", proceedingJoinPoint.signature.name, proceedingJoinPoint.args)

            val res = proceedingJoinPoint.proceed() as Result<*>

            publishEvents(res.events)
            return res
        } catch (e: Throwable) {
            return when (e) {
                is RollbackTransactionException -> error<Unit>(e.status)
                is HttpMessageNotReadableException -> throw e
                is AuthorizationDeniedException -> throw e
                is MethodArgumentNotValidException -> throw e
                else -> {
                    logger.error(e.toString())
                    return error<Unit>(HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        }
    }

    private fun <T: ApplicationEvent> publishEvents(
        events: Collection<T>
    ) = events.forEach{
        eventPublisher.publishEvent(it)
    }
}
