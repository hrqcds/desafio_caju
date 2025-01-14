package br.com.transaction_service.http

import br.com.transaction_service.dtos.inputs.TransactionMCCOutputDTO
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client(id = "http://gateway:9000")
interface ReportHttp {
    @Get("/reports/last-transaction")
    fun getLastTransaction(@QueryValue merchant: String): HttpResponse<TransactionMCCOutputDTO>
}