package br.com.account_service.services

import br.com.account_service.dtos.inputs.CreateAccountInputDTO
import br.com.account_service.dtos.inputs.AddBalanceToAccount
import br.com.account_service.dtos.inputs.UpdateBalanceToAccount
import br.com.account_service.dtos.outputs.AccountOutputDTO
import br.com.account_service.dtos.outputs.AccountOutputWithPaginationDTO
import br.com.account_service.helpers.ErrorResponse
import br.com.account_service.helpers.GetTakePagination
import br.com.account_service.interfaces.IAccountService
import br.com.account_service.repositories.AccountRepository
import br.com.account_service.validations.AccountValidations
import jakarta.inject.Singleton
/*
Adicionar conta OK
Adicionar saldo da conta OK
Buscar dados das contas OK
Listar contas OK
Atualizar saldo da conta


Executar transação de pagamento
Gerar relatório de todas as transações
Gerar relatório de transações por conta

Remover conta
Desativar conta
*/
@Singleton
class AccountService(private val accountRepository: AccountRepository,
                     private val redisService: RedisService) : IAccountService {
    override fun create(request: CreateAccountInputDTO): AccountOutputDTO {
        val result = this.accountRepository.save(CreateAccountInputDTO.generateAccount(request))

        return  AccountOutputDTO.generate(result)
    }

    override fun findByAccountNumber(accountNumber: Long): AccountOutputDTO {
        val result = this.accountRepository.findById(accountNumber)

        if(result.isEmpty)
            throw ErrorResponse("Account not found", 404)

        return AccountOutputDTO.generate(result.get())
    }

    override fun listAll(skip: Int, take: Int): AccountOutputWithPaginationDTO {
        val result = this.accountRepository.findAllPaginated(
            GetTakePagination.getSkip(skip),
            GetTakePagination.getTake(skip, take)
        )

        val total = this.accountRepository.count()

        return AccountOutputWithPaginationDTO.generate(
            result.map { AccountOutputDTO.generate(it) },
            total,
            GetTakePagination.getPage(total, skip, take)
        )
    }

    override fun addBalanceToAccount(request: AddBalanceToAccount): AccountOutputDTO {
        AccountValidations.validateAddBalanceToAccount(request)
        val account = this.accountRepository.findById(request.accountNumber)

        if(account.isEmpty)
            throw ErrorResponse("Account not found", 404)


        val result = this.accountRepository.update(AddBalanceToAccount.generateAccount(account.get(), request))

        redisService.saveAccount(request.accountNumber, AccountOutputDTO.generate(result))

        return AccountOutputDTO.generate(result)
    }

    override fun updateBalanceToAccount(request: UpdateBalanceToAccount): AccountOutputDTO {
        AccountValidations.validateUpdateBalanceToAccount(request)

        val account = this.accountRepository.findById(request.accountNumber)

        if(account.isEmpty)
            throw ErrorResponse("Account not found", 404)

        val result = this.accountRepository.update(UpdateBalanceToAccount.generateAccount(account.get(), request))

        return AccountOutputDTO.generate(result)
    }

}