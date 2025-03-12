package ru.itmo.blpslab1.service.paymentgateway

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.domain.entity.Transaction
import ru.itmo.blpslab1.domain.enums.UserAuthority
import java.util.UUID

@Service
class PaymentGatewayServiceImpl: PaymentGatewayService {

    private object SuperUser: UserDetails{
        private fun readResolve(): Any = SuperUser

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
            return mutableSetOf(UserAuthority.TRANSACTION_ADMIN)
        }

        override fun getPassword(): String = ""

        override fun getUsername(): String = ""

    }

    override fun registerPayment(transaction: Transaction): String{
        return "https://pay-pay-pay.ru/payment/${UUID.randomUUID()}"
    }
}