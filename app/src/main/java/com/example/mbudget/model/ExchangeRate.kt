package com.example.mbudget.model

import java.math.BigDecimal

data class ExchangeRate(
    val rate: BigDecimal,
    val from: Currency,
    val to: Currency,
)
