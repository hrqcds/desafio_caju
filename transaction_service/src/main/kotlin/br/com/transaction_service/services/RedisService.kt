package br.com.transaction_service.services

import br.com.transaction_service.dtos.inputs.UpdateBalanceToAccount
import br.com.transaction_service.dtos.outputs.AccountOutputDTO
import br.com.transaction_service.helpers.ValidDecimal
import io.micronaut.serde.ObjectMapper
import jakarta.inject.Singleton
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

@Singleton
class RedisService(private val objectMapper: ObjectMapper) {
    private val jedisPool = JedisPool(JedisPoolConfig(), "redis", 6379)
    private val jedis = jedisPool.resource

    fun saveAccount(key: Long, value: AccountOutputDTO) {
        val accountJSON = objectMapper.writeValueAsString(value)

        jedis.set(key.toString(), accountJSON)
    }

    fun getAccount(key: Long): AccountOutputDTO {
        val accountJSON = jedis.get(key.toString())
        return objectMapper.readValue(accountJSON, AccountOutputDTO::class.java)
    }

    fun updateAccount(key: Long, value: UpdateBalanceToAccount): AccountOutputDTO {
        // pega o último objeto atualizado
        val accountJSON = jedis.get(key.toString())

        // converte o objeto
        val accountObject = objectMapper.readValue(accountJSON, AccountOutputDTO::class.java)

        // cria um novo objeto com os valores atualizados
        val newObject = AccountOutputDTO(
            accountNumber = accountObject.accountNumber,
            amountFood = ValidDecimal.convert(value.amountFood),
            amountMeal = ValidDecimal.convert(value.amountMeal),
            amountCash = ValidDecimal.convert(value.amountCash),
            username = accountObject.username,
            totalAmount = ValidDecimal.convert(value.amountMeal + value.amountFood + value.amountCash)
        )

        // salva o novo objeto
        jedis.set(key.toString(), objectMapper.writeValueAsString(newObject))

        return newObject
    }
}