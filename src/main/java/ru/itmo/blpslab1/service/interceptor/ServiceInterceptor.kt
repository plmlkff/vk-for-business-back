package ru.itmo.blpslab1.service.interceptor;

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.itmo.blpslab1.service.exceptions.RollbackTransactionException
import ru.itmo.blpslab1.utils.service.*

@Aspect
@Component
class ServiceInterceptor(
    val eventPublisher: ApplicationEventPublisher
){
    val logger: Logger = LoggerFactory.getLogger(ServiceInterceptor::class.java)

    @Pointcut("within(ru.itmo.blpslab1.service..*)")
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
