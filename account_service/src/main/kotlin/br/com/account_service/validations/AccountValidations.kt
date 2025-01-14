package br.com.account_service.validations

import br.com.account_service.dtos.inputs.AddBalanceToAccount
import br.com.account_service.dtos.inputs.UpdateBalanceToAccount
import br.com.account_service.helpers.ErrorResponse

data object AccountValidations {
    fun validateAddBalanceToAccount(balance: AddBalanceToAccount) {
       if(balance.amountMeal < 0 || balance.amountFood < 0 || balance.amountCash < 0)
           throw ErrorResponse("Invalid balance", 400)
    }

    fun validateUpdateBalanceToAccount(balance: UpdateBalanceToAccount) {
       if(balance.amountMeal < 0 || balance.amountFood < 0 || balance.amountCash < 0)
           throw ErrorResponse("Invalid balance", 400)
    }

}