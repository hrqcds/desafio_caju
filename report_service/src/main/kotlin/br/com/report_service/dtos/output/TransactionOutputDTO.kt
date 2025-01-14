package br.com.report_service.dtos.output

import br.com.report_service.entities.Transaction
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable


@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class TransactionMCCOutputDTO(
    val mcc: String,
    val merchant: String
)

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class TransactionOutputDTO(
    val id: String,
    val account: Long,
    val merchant: String,
    val totalAmount: Double,
    val actualAmountFood: Double,
    val actualAmountMeal: Double,
    val actualAmountCash: Double,
    val actualAmount: Double,
    val mcc: String,
    val code: String,
    val transactionDate: String
){
    companion object {
        fun generate(data: Transaction): TransactionOutputDTO{
            return TransactionOutputDTO(
                id = data.id,
                account = data.account,
                merchant = data.merchant,
                totalAmount = data.totalAmount,
                actualAmountCash = data.actualAmountCash,
                actualAmountFood = data.actualAmountFood,
                actualAmountMeal = data.actualAmountMeal,
                actualAmount = data.actualAmount,
                mcc = data.mcc,
                code = data.code,
                transactionDate = data.transactionDate.toString()
            )
        }
    }
}
