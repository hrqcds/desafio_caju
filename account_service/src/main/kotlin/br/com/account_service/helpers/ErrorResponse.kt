package br.com.account_service.helpers

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable.Serializable
data class ApiResponse<T>(val data: T? = null, val error: String? = null)

data class ErrorResponse(override val message: String, val status: Int) : Exception()

fun <T> toApiResponse(errorResponse: ErrorResponse): MutableHttpResponse<ApiResponse<T>> {
    return when (errorResponse.status) {
        400 -> HttpResponse.status<T>(HttpStatus.BAD_REQUEST).body(ApiResponse(error = errorResponse.message))
        401 -> HttpResponse.status<T>(HttpStatus.UNAUTHORIZED).body(ApiResponse(error = errorResponse.message))
        403 -> HttpResponse.status<T>(HttpStatus.FORBIDDEN).body(ApiResponse(error = errorResponse.message))
        404 -> HttpResponse.status<T>(HttpStatus.NOT_FOUND).body(ApiResponse(error = errorResponse.message))
        409 -> HttpResponse.status<T>(HttpStatus.CONFLICT).body(ApiResponse(error = errorResponse.message))
        else -> HttpResponse.serverError(ApiResponse(error = "Erro interno do servidor"))
    }
}

fun <T> toException(e: Exception): MutableHttpResponse<ApiResponse<T>> {
    return when(e){
        is ErrorResponse -> toApiResponse(e)
        else -> HttpResponse.serverError(ApiResponse(error = "Erro interno do servidor"))
    }
}
