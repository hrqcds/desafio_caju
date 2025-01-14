package br.com.transaction_service.dtos.inputs

import br.com.transaction_service.entities.Account
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class CreateAccountInputDTO(
    val username: String
)

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class AddBalanceToAccount(
    val accountNumber: Long,
    val amountMeal: Double,
    val amountFood: Double,
    val amountCash: Double
){
    companion object {
        fun generateAccount(account: Account,newBalance: AddBalanceToAccount, ): Account {
            return Account(
                accountNumber = account.accountNumber,
                username = account.username,
                amountCash = account.amountCash + newBalance.amountCash,
                amountFood = account.amountFood + newBalance.amountFood,
                amountMeal = account.amountMeal + newBalance.amountMeal
            )
        }
    }
}

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class UpdateBalanceToAccount(
    val accountNumber: Long,
    val amountMeal: Double,
    val amountFood: Double,
    val amountCash: Double
){
    companion object {
        fun generateAccount(account: Account, updateValue: UpdateBalanceToAccount): Account {
            return Account(
                accountNumber = account.accountNumber,
                username = account.username,
                amountCash = updateValue.amountCash,
                amountFood = updateValue.amountFood,
                amountMeal = updateValue.amountMeal
            )
        }
    }
}





