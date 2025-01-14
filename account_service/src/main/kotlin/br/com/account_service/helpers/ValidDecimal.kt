package br.com.account_service.helpers

import java.math.BigDecimal
import java.math.RoundingMode

object ValidDecimal {
    fun convert(value: Double): Double {
        return BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}