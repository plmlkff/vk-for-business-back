package ru.itmo.blpslab1.stripe.connection

import com.stripe.Stripe
import com.stripe.exception.StripeException
import com.stripe.model.Charge
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import ru.itmo.blpslab1.utils.service.Result
import ru.itmo.blpslab1.utils.service.error
import ru.itmo.blpslab1.utils.service.ok
import java.util.UUID
import javax.resource.cci.ConnectionMetaData
import javax.resource.cci.Interaction
import javax.resource.cci.LocalTransaction
import javax.resource.cci.ResultSetInfo

class StripeConnectionImpl(
    private val apiKey: String
): StripeConnection {
    private val log: Logger = LoggerFactory.getLogger(StripeConnectionImpl::class.java)

    init {
        Stripe.apiKey = apiKey
    }

    override fun createPayment(productName: String, amount: Int, currency: String, localTransactionId: UUID): Result<Session> {
        Stripe.apiKey = apiKey
        val params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("https://yourdomain.com/success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl("https://yourdomain.com/cancel")
            .setClientReferenceId(localTransactionId.toString())
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(currency)
                            .setUnitAmount(amount.toLong())
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(productName)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build();

        try {
            val session = Session.create(params)
            return ok(session)
        } catch (e: StripeException){
            log.error("Payment creating error: ${e.userMessage}")
            return error(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun createInteraction(): Interaction {
        TODO("Not yet implemented")
    }

    override fun getLocalTransaction(): LocalTransaction {
        TODO("Not yet implemented")
    }

    override fun getMetaData(): ConnectionMetaData {
        TODO("Not yet implemented")
    }

    override fun getResultSetInfo(): ResultSetInfo {
        TODO("Not yet implemented")
    }
}