package ru.itmo.blpslab1.stripe.connectionfactory

import ru.itmo.blpslab1.stripe.connection.StripeConnection
import javax.naming.Reference
import javax.resource.cci.*
import javax.resource.spi.ConnectionManager
import javax.resource.spi.ManagedConnectionFactory

class StripeConnectionFactory(
    private val stripeManagedConnectionFactory: ManagedConnectionFactory,
    private val connectionManager: ConnectionManager? = null
): ConnectionFactory {
    override fun getReference(): Reference? = null

    override fun setReference(reference: Reference?) = Unit

    override fun getConnection(): StripeConnection = stripeManagedConnectionFactory
        .createManagedConnection(null, null)
        .getConnection(null, null) as StripeConnection

    override fun getConnection(properties: ConnectionSpec?) = getConnection()

    override fun getRecordFactory() = null

    override fun getMetaData() = null
}