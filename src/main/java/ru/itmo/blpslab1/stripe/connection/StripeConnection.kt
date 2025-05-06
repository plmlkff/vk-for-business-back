package ru.itmo.blpslab1.stripe.connection

import com.stripe.model.Charge
import com.stripe.model.checkout.Session
import ru.itmo.blpslab1.utils.service.Result
import javax.resource.cci.Connection

interface StripeConnection: Connection {
    fun createPayment(source: String, amount: Int, currency: String): Result<Session>
}