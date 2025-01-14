package br.com.report_service.entities

import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "transactions")
@Serdeable.Deserializable
data class Transaction(
    @Id
    val id: String,

    val account: Long,

    val merchant: String,

    val totalAmount: Double,

    val actualAmountFood: Double = 0.0,

    val actualAmountMeal: Double = 0.0,

    val actualAmountCash: Double = 0.0,

    val actualAmount: Double = actualAmountCash + actualAmountFood + actualAmountMeal,

    val mcc: String,

    val code: String,

    val transactionDate: LocalDateTime,
    )