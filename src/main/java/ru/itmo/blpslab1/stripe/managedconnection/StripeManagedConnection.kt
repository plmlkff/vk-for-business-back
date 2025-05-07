package ru.itmo.blpslab1.stripe.managedconnection

import ru.itmo.blpslab1.stripe.connection.StripeConnection
import ru.itmo.blpslab1.stripe.connection.StripeConnectionImpl
import java.io.PrintWriter
import javax.resource.spi.*
import javax.security.auth.Subject

class StripeManagedConnection(
    private val apiKey: String
): ManagedConnection {
    private var connection: StripeConnection? = null

    private val listeners = mutableSetOf<ConnectionEventListener?>()

    override fun getConnection(p0: Subject?, p1: ConnectionRequestInfo?): StripeConnection {
        this.connection = StripeConnectionImpl(apiKey)

        return connection!!
    }

    override fun destroy() {
        connection = null
    }

    override fun cleanup() {
        destroy()
    }

    override fun associateConnection(p0: Any?) {}

    override fun addConnectionEventListener(listener: ConnectionEventListener?) {
        listeners.add(listener)
    }

    override fun removeConnectionEventListener(listener: ConnectionEventListener?) {
        listeners.remove(listener)
    }

    override fun getXAResource() = null

    override fun getLocalTransaction() = null

    override fun getMetaData() = null

    override fun getLogWriter() = null

    override fun setLogWriter(p0: PrintWriter?) {}
}