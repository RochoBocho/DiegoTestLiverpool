package com.example.diegorochintest.ui.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    return format.format(amount)
}
