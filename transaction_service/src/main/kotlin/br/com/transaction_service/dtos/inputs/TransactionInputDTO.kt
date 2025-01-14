package br.com.transaction_service.dtos.inputs

import br.com.transaction_service.entities.Transaction
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class TransactionInputDTO(
    val account: Long,
    val totalAmount: Double,
    val mcc: String,
    val merchant: String
){
    fun generateWithNewMCC(mcc: String): TransactionInputDTO{
        return TransactionInputDTO(
            account = account,
            merchant = merchant,
            totalAmount = totalAmount,
            mcc = mcc
        )
    }

    companion object {
        fun generateTransaction(transaction: TransactionInputDTO, code: String): Transaction {
            return Transaction(
                account = transaction.account,
                merchant = transaction.merchant,
                totalAmount = transaction.totalAmount,
                mcc = transaction.mcc,
                code = code
            )
        }

    }
}

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class TransactionMCCOutputDTO(
    val mcc: String,
    val merchant: String
)



