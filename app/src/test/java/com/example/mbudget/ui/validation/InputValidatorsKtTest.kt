package com.example.mbudget.ui.validation

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.DecimalFormatSymbols

class InputValidatorsKtTest {
    @Test
    fun isValidDigit_decimalSeparatorDot() {
        assertThat("2.40".isValidDigit()).isTrue()
        assertThat("240".isValidDigit()).isTrue()
        assertThat("2.4.0".isValidDigit()).isFalse()
    }

    @Test
    fun isValidDigit_decimalSeparatorComma() {
        assertThat("2,40".isValidDigit()).isTrue()
        assertThat("240".isValidDigit()).isTrue()
        assertThat("2,4,0".isValidDigit()).isFalse()
    }

    @Test
    fun isValidDigit_decimalSeparatorFromLocale() {

    }
}