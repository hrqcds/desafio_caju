package br.com.transaction_service.produces

import io.micronaut.rabbitmq.annotation.Binding
import io.micronaut.rabbitmq.annotation.RabbitClient


@RabbitClient
interface TransactionProducer {

    @Binding("transactions")
    fun sendTransaction(transaction: ByteArray)
}
