package br.com.transaction_service.validations

import br.com.transaction_service.dtos.inputs.TransactionInputDTO
import br.com.transaction_service.dtos.inputs.UpdateBalanceToAccount
import br.com.transaction_service.dtos.outputs.AccountOutputDTO
import br.com.transaction_service.entities.Transaction
import br.com.transaction_service.helpers.TransactionException
import br.com.transaction_service.helpers.ValidDecimal
import br.com.transaction_service.http.ReportHttp
import br.com.transaction_service.services.RabbitService

object TransactionValidations {
    fun verifyBalance(account: AccountOutputDTO,
                      transaction: TransactionInputDTO,
                      kafkaService: RabbitService,
                      reportHttp: ReportHttp
    ): Pair<UpdateBalanceToAccount, TransactionInputDTO> {

        if(account.totalAmount < transaction.totalAmount){

            kafkaService.sendTransaction(Transaction.generate(account, transaction, "51"))

            throw TransactionException("Saldo insuficiente", "51")
        }

        return when(verifyMcc(transaction)){
            "FOOD" -> verifyAmountFood(account, transaction, kafkaService)
            "MEAL" -> verifyAmountMeal(account, transaction, kafkaService)
            else -> {
                val result = reportHttp.getLastTransaction(transaction.merchant)

                if(result.code() == 200){
                    return when(verifyMccReport(result.body()!!.mcc)){
                        "FOOD" -> verifyAmountFood(account,
                            transaction.generateWithNewMCC(result.body()!!.mcc), kafkaService)
                        "MEAL" -> verifyAmountMeal(account,
                            transaction.generateWithNewMCC(result.body()!!.mcc), kafkaService)
                        else -> verifyAmountCash(account, transaction, kafkaService)
                    }
                }

                verifyAmountCash(account, transaction, kafkaService)
            }
        }

    }

    private fun verifyMcc(transaction: TransactionInputDTO): String{
        return when(transaction.mcc){
            "5411", "5412" -> "FOOD"
            "5811", "5812" -> "MEAL"
            else -> "CASH"

        }
    }

    private fun verifyAmountFood(
        account: AccountOutputDTO,
        transaction: TransactionInputDTO,
        kafkaService: RabbitService
    ): Pair<UpdateBalanceToAccount, TransactionInputDTO>{
        if(account.amountFood >= transaction.totalAmount)
            return Pair(UpdateBalanceToAccount(
                accountNumber = account.accountNumber,
                amountMeal = ValidDecimal.convert(account.amountMeal),
                amountFood = ValidDecimal.convert(account.amountFood - transaction.totalAmount),
                amountCash = ValidDecimal.convert(account.amountCash)
            ), transaction)

        if(account.amountFood < transaction.totalAmount)
            if(account.amountFood + account.amountCash >= transaction.totalAmount){
                return Pair(UpdateBalanceToAccount(
                    accountNumber = account.accountNumber,
                    amountMeal = ValidDecimal.convert(account.amountMeal),
                    amountFood = ValidDecimal.convert(0.0),
                    amountCash = ValidDecimal.convert(account.amountCash - (transaction.totalAmount - account.amountFood))
                ), transaction)
            }

        kafkaService.sendTransaction(Transaction.generate(account, transaction, "51"))
        throw TransactionException("Saldo insuficiente", "51")
    }

    private fun verifyAmountMeal(
        account: AccountOutputDTO,
        transaction: TransactionInputDTO,
        kafkaService: RabbitService): Pair<UpdateBalanceToAccount, TransactionInputDTO>
    {
        if(account.amountMeal >= transaction.totalAmount)
            return Pair(UpdateBalanceToAccount(
                accountNumber = account.accountNumber,
                amountMeal = ValidDecimal.convert(account.amountMeal - transaction.totalAmount),
                amountFood = ValidDecimal.convert(account.amountFood),
                amountCash = ValidDecimal.convert(account.amountCash)
            ), transaction)

        if(account.amountMeal < transaction.totalAmount)
            if(account.amountMeal + account.amountCash >= transaction.totalAmount){
                return Pair(UpdateBalanceToAccount(
                    accountNumber = account.accountNumber,
                    amountMeal = ValidDecimal.convert(0.0),
                    amountFood = ValidDecimal.convert(account.amountFood),
                    amountCash = ValidDecimal.convert(account.amountCash - (transaction.totalAmount - account.amountMeal))
                ), transaction)
            }

        kafkaService.sendTransaction(Transaction.generate(account, transaction, "51"))
        throw TransactionException("Saldo insuficiente", "51")
    }

    private fun verifyAmountCash(
        account: AccountOutputDTO,
        transaction: TransactionInputDTO,
        kafkaService: RabbitService): Pair<UpdateBalanceToAccount, TransactionInputDTO>
    {
        if(account.amountCash >= transaction.totalAmount)
            return Pair(
                UpdateBalanceToAccount(
                    accountNumber = account.accountNumber,
                    amountMeal = ValidDecimal.convert(account.amountMeal),
                    amountFood = ValidDecimal.convert(account.amountFood),
                    amountCash = ValidDecimal.convert(account.amountCash - transaction.totalAmount)
                ),
                transaction
            )

        kafkaService.sendTransaction(Transaction.generate(account, transaction, "51"))
        throw TransactionException("Saldo insuficiente", "51")
    }

    private fun verifyMccReport(mcc: String): String{
        return when(mcc){
            "5411", "5412" -> "FOOD"
            "5811", "5812" -> "MEAL"
            else -> "CASH"
        }
    }
}


