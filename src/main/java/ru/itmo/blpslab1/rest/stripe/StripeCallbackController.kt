package ru.itmo.blpslab1.rest.stripe

import com.stripe.model.Event
import com.stripe.net.Webhook
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import ru.itmo.blpslab1.stripe.connection.StripeConnectionImpl

@RestController
@RequestMapping("/api/stripe")
class StripeCallbackController(
    @Value("\${stripe.webhooks.secret}")
    private val apiSecret: String
) {
    @PostMapping("/webhook")
    fun handleStripeEvent(
        @RequestBody event: Event,
    ): String {
        val connection = StripeConnectionImpl("sk_test_51RLhMH4DXejGDqcBN9eyvldNSLpl2lpamk0otkCa8X1cw1xOR5ivO21qVAIwX7nJUBIg06bN2mYYe2739S6nTflA004jtnarzW")
        val session = connection.createPayment("tok_visa", 1111, "usd")
        return event.type
    }
}