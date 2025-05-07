package ru.itmo.blpslab1.service.paymentgateway

import ru.itmo.blpslab1.domain.entity.Transaction

interface PaymentGatewayService {
    fun registerPayment(transaction: Transaction, productName: String): String
}