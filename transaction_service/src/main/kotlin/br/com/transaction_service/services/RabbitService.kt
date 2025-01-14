package br.com.transaction_service.services

import br.com.transaction_service.entities.Transaction
import br.com.transaction_service.produces.TransactionProducer
import io.micronaut.serde.ObjectMapper
import jakarta.inject.Singleton

@Singleton
class RabbitService(
    private val objectMapper: ObjectMapper,
    private val transactionProducer: TransactionProducer
) {

    fun sendTransaction(transaction: Transaction){
        val transactionJSON = objectMapper.writeValueAsBytes(transaction)
        transactionProducer.sendTransaction(transactionJSON)
    }

}