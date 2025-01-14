package br.com.transaction_service.entities

import java.util.*

data class Account(
    val accountNumber: Long,

    val username: String,

    val amountFood: Double,

    val amountMeal: Double,

    val amountCash: Double,
    )
