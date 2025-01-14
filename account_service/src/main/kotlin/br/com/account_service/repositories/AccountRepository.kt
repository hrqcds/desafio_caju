package br.com.account_service.repositories

import br.com.account_service.entities.Account
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    @Query("SELECT * FROM accounts ORDER BY account_number OFFSET :skip LIMIT :take", nativeQuery = true)
    fun findAllPaginated(skip: Int, take: Int): List<Account>
}