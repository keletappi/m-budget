package com.example.mbudget.model

import java.math.BigDecimal

data class Amount(
    val amount: BigDecimal,
    val currency: Currency,
) {
    constructor(amount: String, currency: Currency): this(amount.toBigDecimal(), currency)
}