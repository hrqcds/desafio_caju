package br.com.transaction_service.http

import br.com.transaction_service.dtos.inputs.UpdateBalanceToAccount
import br.com.transaction_service.dtos.outputs.AccountOutputDTO
import br.com.transaction_service.helpers.ApiResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Patch
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client


interface IAccount {
    fun findByAccountNumber(accountNumber: Long): HttpResponse<ApiResponse<AccountOutputDTO>>
    fun updateBalance(request: UpdateBalanceToAccount): HttpResponse<ApiResponse<AccountOutputDTO>>
}


@Client(id = "http://gateway:9000")
interface AccountHttp : IAccount {

        @Get("/accounts/{accountNumber}")
        override fun findByAccountNumber(@PathVariable accountNumber: Long): HttpResponse<ApiResponse<AccountOutputDTO>>

        @Patch("/accounts/update-balance")
        override fun updateBalance(@Body request: UpdateBalanceToAccount): HttpResponse<ApiResponse<AccountOutputDTO>>
}