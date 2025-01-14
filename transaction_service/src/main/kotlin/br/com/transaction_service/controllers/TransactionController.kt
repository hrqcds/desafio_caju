package br.com.transaction_service.controllers

import br.com.transaction_service.dtos.inputs.TransactionInputDTO
import br.com.transaction_service.dtos.outputs.AccountOutputDTO
import br.com.transaction_service.helpers.ApiResponse
import br.com.transaction_service.helpers.TransactionResponse
import br.com.transaction_service.helpers.toApiResponse
import br.com.transaction_service.helpers.toTransactionException
import br.com.transaction_service.services.TransactionService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

@Controller("/")
@ExecuteOn(TaskExecutors.BLOCKING)
class TransactionController(private val transactionService: TransactionService) {
    @Post
    fun getAccount(@Body request: TransactionInputDTO): HttpResponse<TransactionResponse> {
        return try {
            transactionService.execute(request)
            HttpResponse.ok(TransactionResponse("00"))
        }catch (e: Exception){
            return toTransactionException(e)
        }
    }
}