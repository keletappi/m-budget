package com.example.mbudget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mbudget.model.Currency
import com.example.mbudget.model.EUR
import com.example.mbudget.ui.util.formattedAs
import java.math.BigDecimal

@Composable
fun BudgetHeader(
    name: String,
    used: BigDecimal,
    limit: BigDecimal,
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h5,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 16.dp),
            text = buildAnnotatedString {
                val usedAmountColor = if (used > limit) Color.Red else Color.Green
                withStyle(style = SpanStyle(color = usedAmountColor)) {
                    append(used.formattedAs(currency))
                }
                append(" / ")
                append(limit.formattedAs(currency))
            }
        )
    }
}

@Composable
@Preview(widthDp = 320)
fun PreviewBudgetHeader() {
    MaterialTheme {
        BudgetHeader(
            name = "Preview Budget",
            used = 40.toBigDecimal(),
            limit = 120.toBigDecimal(),
            currency = EUR
        )
    }
}
