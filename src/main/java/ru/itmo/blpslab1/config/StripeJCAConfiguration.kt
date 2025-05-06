package ru.itmo.blpslab1.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itmo.blpslab1.stripe.connectionfactory.StripeConnectionFactory
import ru.itmo.blpslab1.stripe.managedconnectionfactory.StripeManagedConnectionFactory

@Configuration
class StripeJCAConfiguration(
    @Value("\${stripe.api.secret}")
    private val secretKey: String
) {
    @Bean
    fun getStripeConnectionFactory(
        stripeManagedConnectionFactory: StripeManagedConnectionFactory
    ): StripeConnectionFactory = StripeConnectionFactory(stripeManagedConnectionFactory, null)

    @Bean
    fun getManagedConnectionFactory(): StripeManagedConnectionFactory = StripeManagedConnectionFactory(secretKey)
}