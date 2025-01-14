package br.com.account_service.controllers

import br.com.account_service.dtos.inputs.AddBalanceToAccount
import br.com.account_service.dtos.inputs.CreateAccountInputDTO
import br.com.account_service.dtos.inputs.UpdateBalanceToAccount
import br.com.account_service.dtos.outputs.AccountOutputDTO
import br.com.account_service.dtos.outputs.AccountOutputWithPaginationDTO
import br.com.account_service.helpers.ApiResponse
import br.com.account_service.helpers.toException
import br.com.account_service.services.AccountService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Patch
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue

@Controller("/")
class AccountController(private val accountService: AccountService) {

    @Post
    fun create(@Body request: CreateAccountInputDTO): MutableHttpResponse<ApiResponse<AccountOutputDTO>>{
       return try {
           HttpResponse.created(ApiResponse(accountService.create(request)))
       } catch (e: Exception){
           return toException(e)
       }
    }

    @Patch("/add-balance")
    fun addBalance(@Body request: AddBalanceToAccount): MutableHttpResponse<ApiResponse<AccountOutputDTO>>{
       return try {
           HttpResponse.ok(ApiResponse(accountService.addBalanceToAccount(request)))
       } catch (e: Exception){
              return toException(e)
       }
    }

    @Patch("/update-balance")
    fun updateBalance(@Body request: UpdateBalanceToAccount): MutableHttpResponse<ApiResponse<AccountOutputDTO>>{
       return try {
           HttpResponse.ok(ApiResponse(accountService.updateBalanceToAccount(request)))
       } catch (e: Exception){
           return toException(e)
       }
    }

    @Get("/{accountNumber}")
    fun findByAccountNumber(@PathVariable accountNumber: Long): MutableHttpResponse<ApiResponse<AccountOutputDTO>>{
       return try {
           HttpResponse.ok(ApiResponse(accountService.findByAccountNumber(accountNumber)))
       } catch (e: Exception){
           return toException(e)
       }
    }

    @Get("/list")
    fun listAll(@QueryValue skip: Int,@QueryValue take: Int): MutableHttpResponse<ApiResponse<AccountOutputWithPaginationDTO>>{
       return try {
           HttpResponse.ok(ApiResponse(accountService.listAll(skip, take)))
       } catch (e: Exception){
           return toException(e)
       }
    }


}