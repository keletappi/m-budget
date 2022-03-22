package com.example.mbudget.util

import java.math.BigDecimal

infix fun BigDecimal.comparesEqual(other: BigDecimal): Boolean = this.compareTo(other) == 0
infix fun BigDecimal.comparesNotEqual(other: BigDecimal): Boolean = !this.comparesEqual(other)
