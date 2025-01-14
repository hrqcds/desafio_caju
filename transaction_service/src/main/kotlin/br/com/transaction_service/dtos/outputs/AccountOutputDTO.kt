package br.com.transaction_service.dtos.outputs

import br.com.transaction_service.entities.Account
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class AccountOutputDTO(
    val accountNumber: Long,

    val username: String,

    val amountFood: Double,

    val amountMeal: Double,

    val amountCash: Double,

    val totalAmount: Double
){

    companion object {
        fun generate(data: Account): AccountOutputDTO{
            val total = data.amountMeal + data.amountCash + data.amountFood
            return AccountOutputDTO(
                username = data.username,
                amountCash = data.amountCash,
                amountFood = data.amountFood,
                amountMeal = data.amountMeal,
                accountNumber = data.accountNumber,
                totalAmount = total
            )
        }
    }

}

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class AccountOutputWithPaginationDTO(
    val accounts: List<AccountOutputDTO>,
    val total: Long,
    val page: Int
){
    companion object {
        fun generate(accounts: List<AccountOutputDTO>, total: Long, page: Int): AccountOutputWithPaginationDTO{
            return AccountOutputWithPaginationDTO(
                accounts = accounts,
                total = total,
                page = page.let { if(it == 0) 1 else it + 1 }
            )
        }
    }
}

