package br.com.account_service.services

import br.com.account_service.dtos.outputs.AccountOutputDTO
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

}