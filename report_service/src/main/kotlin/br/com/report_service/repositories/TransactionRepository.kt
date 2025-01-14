package br.com.report_service.repositories

import br.com.report_service.entities.Transaction
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface TransactionRepository : JpaRepository<Transaction, String> {

    @Query("SELECT * FROM transactions t WHERE t.merchant = :merchant AND t.code = '00' " +
            "ORDER BY t.transaction_date DESC LIMIT 1", nativeQuery = true)
    fun findLastMerchant(merchant: String): List<Transaction>

    @Query("SELECT * FROM transactions t WHERE DATE(t.transaction_date) " +
            "BETWEEN DATE(:start) AND DATE(:end) " +
            "ORDER BY t.transaction_date", nativeQuery = true)
    fun findByTransactionDateBetween(start: String, end: String): List<Transaction>

    @Query("SELECT * FROM transactions t WHERE " +
            "t.account = :account AND DATE(t.transaction_date) " +
            "BETWEEN DATE(:start) AND DATE(:end) " +
            "ORDER BY t.transaction_date", nativeQuery = true)
    fun findByAccountAndTransactionDateBetween(
        account: Long, start: String, end: String): List<Transaction>
}


