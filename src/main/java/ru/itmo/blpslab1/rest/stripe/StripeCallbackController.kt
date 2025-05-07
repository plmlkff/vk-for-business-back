package ru.itmo.blpslab1.rest.stripe

import com.stripe.model.Event
import com.stripe.model.PaymentIntent
import com.stripe.model.checkout.Session
import com.stripe.net.Webhook
import com.stripe.param.checkout.SessionListParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import ru.itmo.blpslab1.domain.enums.TransactionState
import ru.itmo.blpslab1.rest.dto.response.TransactionResponse
import ru.itmo.blpslab1.service.TransactionService
import ru.itmo.blpslab1.utils.service.Result
import ru.itmo.blpslab1.utils.service.ok
import java.util.*

@RestController
@RequestMapping("/api/stripe")
class StripeCallbackController(
    private val transactionService: TransactionService,
    @Value("\${stripe.webhooks.secret}")
    private val webhookSecretKey: String
) {
    @PostMapping("/webhook")
    fun handleStripeEvent(
        @RequestBody payload: String,
        @RequestHeader("Stripe-Signature") sigHeader: String
    ): Result<TransactionResponse> {
        val event = Webhook.constructEvent(payload, sigHeader, webhookSecretKey)

        if (event.type !in setOf("checkout.session.completed", "payment_intent.payment_failed", "checkout.session.expired")) return ok()

        val session = getSessionObject(event)

        return transactionService.editTransactionState(
            id = UUID.fromString(session.clientReferenceId),
            newState = transactionTypeFromEventType(event.type)
        )
    }

    private fun getSessionObject(event: Event): Session{
        val obj = event.dataObjectDeserializer.`object`.orElseThrow()
        if (obj is Session) return obj

        val paymentIntent = obj as PaymentIntent

        val params = SessionListParams.builder().setPaymentIntent(paymentIntent.id).build()

        return Session.list(params).data.get(0)
    }

    private fun transactionTypeFromEventType(type: String) = when(type){
        "checkout.session.completed" -> TransactionState.PAID
        "payment_intent.payment_failed" -> TransactionState.FAILED
        "checkout.session.expired" -> TransactionState.CANCELED
        else -> throw IllegalArgumentException()
    }
}