package ru.itmo.blpslab1.config

import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.SimpleScheduleBuilder
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itmo.blpslab1.core.quartz.job.SubscriptionDeactivatorJob

@Configuration
class QuartzConfig(
    @Value("\${spring.quartz.subscription-deactivator-job-period}")
    private val subscriptionDeactivatorTriggerPeriod: Int
) {

    @Bean
    fun getJobDetail() = JobBuilder.newJob(SubscriptionDeactivatorJob::class.java)
        .storeDurably()
        .build()

    @Bean
    fun getSubscriptionDeactivatorJobTrigger(jobDetail: JobDetail): Trigger = TriggerBuilder
        .newTrigger()
        .forJob(jobDetail)
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(subscriptionDeactivatorTriggerPeriod)
            .repeatForever()
        ).build()
}