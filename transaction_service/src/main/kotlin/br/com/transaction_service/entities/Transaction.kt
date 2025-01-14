package br.com.transaction_service.entities

import br.com.transaction_service.dtos.inputs.TransactionInputDTO
import br.com.transaction_service.dtos.outputs.AccountOutputDTO
import br.com.transaction_service.helpers.ValidDecimal
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime
import java.util.*

@Serdeable.Serializable
data class Transaction(
    val id: String = UUID.randomUUID().toString(),

    val account: Long,

    val merchant: String,

    val totalAmount: Double,

    val actualAmountFood: Double = 0.0,

    val actualAmountMeal: Double = 0.0,

    val actualAmountCash: Double = 0.0,

    val actualAmount: Double = actualAmountCash + actualAmountFood + actualAmountMeal,

    val mcc: String,

    val code: String,

    val transactionDate: LocalDateTime = LocalDateTime.now(),

    ){
    companion object {
        fun generate(account: AccountOutputDTO, transaction: TransactionInputDTO, code: String): Transaction {
            return Transaction(
                account = transaction.account,
                merchant = transaction.merchant,
                totalAmount = ValidDecimal.convert(transaction.totalAmount),
                mcc = transaction.mcc,
                code = code,
                actualAmountCash = ValidDecimal.convert(account.amountCash),
                actualAmountFood = ValidDecimal.convert(account.amountFood),
                actualAmountMeal = ValidDecimal.convert(account.amountMeal),
            )
        }

    }
}


