package com.example.mbudget.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.mbudget.model.*
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.util.*
import kotlin.text.Typography.nbsp

class ExpenseRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sameCurrency() {
        Locale.setDefault(Locale.US)
        with(composeTestRule) {
            setContent {
                MaterialTheme {
                    ExpenseRow(
                        expense = Expense(
                            name = "Test Expense",
                            amount = Amount(
                                amount = "11.22".toBigDecimal(), EUR
                            ),
                            time = Instant.parse("2022-03-20T15:03:43Z")
                        ),
                        budgetCurrency = EUR
                    )
                }
            }

            onNode(hasTestTag(ExpenseRowSemantics.TestTag.NAME)).assertTextEquals("Test Expense")
            onNode(hasTestTag(ExpenseRowSemantics.TestTag.AMOUNT_BUDGET_CURRENCY))
                .assertTextEquals("€11.22")
            onNode(hasTestTag(ExpenseRowSemantics.TestTag.AMOUNT_ORIGINAL_CURRENCY)).assertDoesNotExist()
        }
    }

    @Test
    fun sameCurrencyInFinland() {
        Locale.setDefault(Locale("fi", "FI"))
        with(composeTestRule) {
            setContent {
                MaterialTheme {
                    ExpenseRow(
                        expense = Expense(
                            name = "Test Expense",
                            amount = Amount(
                                amount = "11.22".toBigDecimal(), EUR
                            ),
                            time = Instant.parse("2022-03-20T15:03:43Z")
                        ),
                        budgetCurrency = EUR
                    )
                }
            }

            onNode(hasTestTag(ExpenseRowSemantics.TestTag.NAME)).assertTextEquals("Test Expense")
            onNode(hasTestTag(ExpenseRowSemantics.TestTag.AMOUNT_BUDGET_CURRENCY))
                .assertTextEquals("11,22${nbsp}€")
            onNode(hasTestTag(ExpenseRowSemantics.TestTag.AMOUNT_ORIGINAL_CURRENCY)).assertDoesNotExist()
        }

    }

    @Test
    fun differentCurrency() {
        Locale.setDefault(Locale.US)
        with(composeTestRule) {
            setContent {
                MaterialTheme {
                    ExpenseRow(
                        expense = Expense(
                            name = "Test Expense",
                            amount = Amount(
                                amount = "11.22".toBigDecimal(), EUR
                            ),
                            time = Instant.parse("2022-03-20T15:03:43Z"),
                            exchangeRate = ExchangeRate(10.toBigDecimal(), from = EUR, to = SEK)
                        ),
                        budgetCurrency = SEK
                    )
                }
            }

            onNode(hasTestTag(ExpenseRowSemantics.TestTag.NAME)).assertTextEquals("Test Expense")
            onNode(hasTestTag(ExpenseRowSemantics.TestTag.AMOUNT_BUDGET_CURRENCY))
                .assertTextEquals("SEK112.20")
            onNode(hasTestTag(ExpenseRowSemantics.TestTag.AMOUNT_ORIGINAL_CURRENCY))
                .assertTextEquals("€11.22")
        }
    }

}