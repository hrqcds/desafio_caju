package br.com.report_service.controllers

import br.com.report_service.dtos.output.TransactionMCCOutputDTO
import br.com.report_service.dtos.output.TransactionOutputDTO
import br.com.report_service.helpers.ApiResponse
import br.com.report_service.helpers.toException
import br.com.report_service.services.TransactionService
import io.micronaut.context.annotation.Parameter
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.util.Date

@Controller("/")
class TransactionController(
    private val transactionService: TransactionService
) {

    @Get("/transactions")
    fun getTransactions(@QueryValue start: String, @QueryValue end: String): HttpResponse<ApiResponse<List<TransactionOutputDTO>>>{
        return try {
            val transactions = transactionService.getAllTransactions(start, end)
            HttpResponse.ok(ApiResponse(transactions))
        } catch (e: Exception){
            return toException(e)
        }
    }

    @Get("/transactions/{account}")
    fun getTransaction(@Parameter account: Long, @QueryValue start: String, @QueryValue end: String):
            HttpResponse<ApiResponse<List<TransactionOutputDTO>>> {
        return try {
            val transactions = transactionService.getTransactionsByAccount(account, start, end)
            HttpResponse.ok(ApiResponse(transactions))
        } catch (e: Exception){
            return toException(e)
        }
    }

    @Get("/last-transaction")
    fun getLastTransaction(@QueryValue merchant: String): MutableHttpResponse<TransactionMCCOutputDTO>{
        return try {
            val transaction = transactionService.getMcc(merchant)
            HttpResponse.ok(transaction)
        } catch (e: Exception){
            return HttpResponse.notFound()
        }
    }

}