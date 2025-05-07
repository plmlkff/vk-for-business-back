package ru.itmo.blpslab1.core.quartz.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import ru.itmo.blpslab1.domain.repository.SubscriptionRepository
import ru.itmo.blpslab1.kafka.event.subscription.SubscriptionsExpiredEvent
import ru.itmo.blpslab1.kafka.service.SubscriptionKafkaEventService

@Component
class SubscriptionDeactivatorJob(
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionKafkaEventService: SubscriptionKafkaEventService
): Job {
    override fun execute(context: JobExecutionContext?) {
        val deactivatedSubscriptionIds = subscriptionRepository.findAndDeactivateExpiredSubscriptionsAndGetTheirIds()
        if (deactivatedSubscriptionIds.isEmpty()) return

        subscriptionKafkaEventService.sendSubscriptionEvent(SubscriptionsExpiredEvent(deactivatedSubscriptionIds))
    }
}