package br.com.transaction_service.helpers

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class TransactionResponse(
    val code: String
)

data class TransactionException(override val message: String, val code: String) : Exception()

fun toTransactionException(e: Exception): HttpResponse<TransactionResponse>{
    return when(e){
        is TransactionException -> HttpResponse.ok(TransactionResponse(e.code))
        else -> {
            println(e)
            HttpResponse.ok(TransactionResponse("07"))
        }

    }
}
