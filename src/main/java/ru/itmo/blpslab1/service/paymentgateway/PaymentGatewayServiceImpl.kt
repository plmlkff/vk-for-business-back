package ru.itmo.blpslab1.service.paymentgateway

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.domain.entity.Transaction
import ru.itmo.blpslab1.service.exceptions.RollbackTransactionException
import ru.itmo.blpslab1.stripe.connectionfactory.StripeConnectionFactory

@Service
class PaymentGatewayServiceImpl(
    private val stripeConnectionFactory: StripeConnectionFactory
): PaymentGatewayService {
    companion object{
        const val KOPECK_IN_RUBLE = 100

        const val CURRENCY_RUB = "rub"
    }

    override fun registerPayment(transaction: Transaction, productName: String): String{
        val connection = stripeConnectionFactory.connection

        val session = connection.createPayment(productName, transaction.amount.toInt() * KOPECK_IN_RUBLE, CURRENCY_RUB, transaction.id)

        if (session.isError()) throw RollbackTransactionException(HttpStatus.INTERNAL_SERVER_ERROR)

        return session.body!!.url
    }
}