package ru.itmo.blpslab1.stripe.resourceadapter

import javax.resource.spi.ActivationSpec
import javax.resource.spi.BootstrapContext
import javax.resource.spi.ResourceAdapter
import javax.resource.spi.endpoint.MessageEndpointFactory
import javax.transaction.xa.XAResource

class StripeResourceAdapter: ResourceAdapter {
    override fun start(ctx: BootstrapContext?) = Unit

    override fun stop() = Unit

    override fun endpointActivation(endpointFactory: MessageEndpointFactory?, spec: ActivationSpec?) = Unit

    override fun endpointDeactivation(endpointFactory: MessageEndpointFactory?, spec: ActivationSpec?) = Unit

    override fun getXAResources(specs: Array<out ActivationSpec>?): Array<XAResource>? = null
}