package br.com.account_service.dtos.outputs

import br.com.account_service.entities.Account
import br.com.account_service.helpers.ValidDecimal
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable.Serializable
data class AccountOutputDTO(
    val accountNumber: Long?,

    val username: String,

    val amountFood: Double,

    val amountMeal: Double,

    val amountCash: Double,

    val totalAmount: Double
){

    companion object {
        fun generate(data: Account): AccountOutputDTO{
            val total = ValidDecimal.convert(data.amountMeal + data.amountCash + data.amountFood)
            return AccountOutputDTO(
                username = data.username,
                amountCash = ValidDecimal.convert(data.amountCash),
                amountFood = ValidDecimal.convert(data.amountFood),
                amountMeal = ValidDecimal.convert(data.amountMeal),
                accountNumber = data.accountNumber,
                totalAmount = total
            )
        }
    }

}

@Introspected
@Serdeable.Serializable
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

