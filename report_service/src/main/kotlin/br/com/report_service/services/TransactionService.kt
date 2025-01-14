package br.com.report_service.services

import br.com.report_service.dtos.output.TransactionMCCOutputDTO
import br.com.report_service.dtos.output.TransactionOutputDTO
import br.com.report_service.helpers.ErrorResponse
import br.com.report_service.repositories.TransactionRepository
import jakarta.inject.Singleton
import java.util.*

@Singleton
class TransactionService(private val transactionRepository: TransactionRepository) {

    fun getMcc(merchant: String): TransactionMCCOutputDTO {
        val transaction = transactionRepository.findLastMerchant(merchant)

        if (transaction.isEmpty()){
            throw ErrorResponse("Merchant not found", 404)
        }

        return TransactionMCCOutputDTO(
            mcc = transaction[0].mcc,
            merchant = transaction[0].merchant
        )
    }

    fun getAllTransactions(start: String, end: String): List<TransactionOutputDTO> {
        val transactions = transactionRepository.findByTransactionDateBetween(start, end)

        return transactions.map { TransactionOutputDTO.generate(it) }
    }

    fun getTransactionsByAccount(account: Long, start: String, end: String): List<TransactionOutputDTO> {
        val transactions = transactionRepository.findByAccountAndTransactionDateBetween(account, start, end)

        return transactions.map { TransactionOutputDTO.generate(it) }
    }

}