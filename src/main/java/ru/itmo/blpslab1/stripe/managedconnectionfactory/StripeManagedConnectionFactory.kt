package ru.itmo.blpslab1.stripe.managedconnectionfactory

import ru.itmo.blpslab1.stripe.connectionfactory.StripeConnectionFactory
import ru.itmo.blpslab1.stripe.managedconnection.StripeManagedConnection
import java.io.PrintWriter
import javax.resource.cci.ConnectionFactory
import javax.resource.spi.*
import javax.security.auth.Subject

class StripeManagedConnectionFactory(
    private val apiKey: String
): ManagedConnectionFactory {
    override fun createConnectionFactory(): ConnectionFactory = StripeConnectionFactory(this)

    override fun createConnectionFactory(cxManager: ConnectionManager?): ConnectionFactory = StripeConnectionFactory(this, cxManager)

    override fun createManagedConnection(
        subject: Subject?, cxRequestInfo: ConnectionRequestInfo?
    ): ManagedConnection = StripeManagedConnection(apiKey)

    override fun matchManagedConnections(
        connectionSet: MutableSet<Any?>?,
        subject: Subject?,
        cxRequestInfo: ConnectionRequestInfo?
    ): ManagedConnection? = null

    override fun getLogWriter(): PrintWriter? = null

    override fun setLogWriter(out: PrintWriter?) = Unit
}