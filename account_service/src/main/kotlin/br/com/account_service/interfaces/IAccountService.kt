package br.com.account_service.interfaces

import br.com.account_service.dtos.inputs.CreateAccountInputDTO
import br.com.account_service.dtos.inputs.AddBalanceToAccount
import br.com.account_service.dtos.inputs.UpdateBalanceToAccount
import br.com.account_service.dtos.outputs.AccountOutputDTO
import br.com.account_service.dtos.outputs.AccountOutputWithPaginationDTO

interface IAccountService {
    fun create(request: CreateAccountInputDTO ): AccountOutputDTO
    fun findByAccountNumber(accountNumber: Long): AccountOutputDTO
    fun listAll(skip: Int, take: Int): AccountOutputWithPaginationDTO
    fun addBalanceToAccount(request: AddBalanceToAccount): AccountOutputDTO
    fun updateBalanceToAccount(request: UpdateBalanceToAccount): AccountOutputDTO
}