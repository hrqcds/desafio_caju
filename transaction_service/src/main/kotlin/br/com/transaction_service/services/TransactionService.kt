package br.com.transaction_service.services

import br.com.transaction_service.dtos.inputs.TransactionInputDTO
import br.com.transaction_service.dtos.outputs.AccountOutputDTO
import br.com.transaction_service.entities.Transaction
import br.com.transaction_service.helpers.ApiResponse
import br.com.transaction_service.helpers.ErrorResponse
import br.com.transaction_service.http.ReportHttp
import br.com.transaction_service.http.circuit_breaker.AccountHttpFallback
import br.com.transaction_service.validations.TransactionValidations
import io.micronaut.http.HttpResponse
import jakarta.inject.Singleton

@Singleton
class TransactionService(
    private val accountHttpFallback: AccountHttpFallback,
    private val reportHttp: ReportHttp,
    private val rabbitService: RabbitService) {
        fun execute(request: TransactionInputDTO): HttpResponse<ApiResponse<AccountOutputDTO>> {
            // buscando dados da conta
            val resultFindAccount = accountHttpFallback.findByAccountNumber(request.account)
            // verificando se a conta existe
            if (resultFindAccount.body().data == null){
                rabbitService.sendTransaction(
                    Transaction.generate(
                        AccountOutputDTO(
                            0, "Conta não encontrada",
                            0.0, 0.0, 0.0, 0.0
                        ), request, "07"
                    )
                )
                throw ErrorResponse("Conta não encontrada", 404)
            }

            // verificando se a conta tem saldo suficiente
            val (output, transaction) = TransactionValidations.verifyBalance(
                resultFindAccount.body().data!!,
                request,
                rabbitService,
                reportHttp)

            // atualizando saldo da conta
            val resultUpdateAmount = accountHttpFallback.updateBalance(output)

            // verificando se a conta foi atualizada
            if (resultUpdateAmount.body().data == null)
                throw ErrorResponse("Erro ao atualizar saldo da conta", 500)


            rabbitService.sendTransaction(
                Transaction.generate(
                    resultUpdateAmount.body().data!!, transaction, "00"
                )
            )

            return resultUpdateAmount
        }
}