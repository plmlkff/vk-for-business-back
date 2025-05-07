package ru.itmo.blpslab1.stripe.connection

import com.stripe.model.checkout.Session
import ru.itmo.blpslab1.utils.service.Result
import java.util.*
import javax.resource.cci.Connection

interface StripeConnection: Connection {
    fun createPayment(productName: String, amount: Int, currency: String, localTransactionId: UUID): Result<Session>
}