package br.com.account_service.dtos.inputs

import br.com.account_service.entities.Account
import br.com.account_service.helpers.ValidDecimal
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable.Deserializable
data class CreateAccountInputDTO(
    val username: String
){
    companion object {
        fun generateAccount(data: CreateAccountInputDTO): Account {
            return Account(username = data.username)
        }
    }

}

@Introspected
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
                amountCash = ValidDecimal.convert(updateValue.amountCash),
                amountFood = ValidDecimal.convert(updateValue.amountFood),
                amountMeal = ValidDecimal.convert(updateValue.amountMeal)
            )
        }
    }
}





