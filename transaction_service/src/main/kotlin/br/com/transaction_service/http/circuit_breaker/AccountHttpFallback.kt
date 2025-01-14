package br.com.transaction_service.http.circuit_breaker

import br.com.transaction_service.dtos.inputs.UpdateBalanceToAccount
import br.com.transaction_service.dtos.outputs.AccountOutputDTO
import br.com.transaction_service.helpers.ApiResponse
import br.com.transaction_service.helpers.ValidDecimal
import br.com.transaction_service.http.AccountHttp
import br.com.transaction_service.services.RedisService
import io.micronaut.http.HttpResponse
import jakarta.inject.Singleton

@Singleton
class AccountHttpFallback(
        private val accountHttp: AccountHttp,
        private val redisService: RedisService,
    ) {
    fun findByAccountNumber(accountNumber: Long): HttpResponse<ApiResponse<AccountOutputDTO>> {
        try {

            val result = accountHttp.findByAccountNumber(accountNumber)

            val existResult = redisService.getAccount(accountNumber)

            if(result.body().data!!.totalAmount > existResult.totalAmount) {
                accountHttp.updateBalance(UpdateBalanceToAccount(
                    accountNumber = accountNumber,
                    amountFood = ValidDecimal.convert(existResult.amountFood),
                    amountMeal = ValidDecimal.convert(existResult.amountMeal),
                    amountCash = ValidDecimal.convert(existResult.amountCash)
                ))

                redisService.saveAccount(accountNumber, existResult)

                return HttpResponse.ok(ApiResponse(existResult))
            }

            redisService.saveAccount(accountNumber, result.body().data!!)

            return result
        }catch (e: Exception){
            val accountDTO = redisService.getAccount(accountNumber)

            return HttpResponse.ok(ApiResponse(accountDTO))
        }

    }

    fun updateBalance(request: UpdateBalanceToAccount): HttpResponse<ApiResponse<AccountOutputDTO>> {
        try {
            val response = accountHttp.updateBalance(request)

            redisService.updateAccount(request.accountNumber, request)

            return response
        }catch(e: Exception) {
            val accountDTO = redisService.updateAccount(request.accountNumber, request)

            return HttpResponse.ok(ApiResponse(accountDTO))
        }
    }
}