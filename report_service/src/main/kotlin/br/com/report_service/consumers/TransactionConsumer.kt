package br.com.report_service.consumers

import br.com.report_service.entities.Transaction
import br.com.report_service.repositories.TransactionRepository
import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener
import io.micronaut.serde.ObjectMapper

@RabbitListener
class TransactionConsumer(
    private val transactionRepository: TransactionRepository,
    private val objectMapper: ObjectMapper
){

    @Queue("transactions")
    fun receiveTransaction(transaction: ByteArray) {
        val transactionObject = objectMapper.readValue(transaction, Transaction::class.java)
        println("Received transaction: $transactionObject")
        transactionRepository.save(transactionObject)
        println("Transaction saved")
    }
}