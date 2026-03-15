package com.example.coffeeshop

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyUtils {
    private val localeID = Locale("in", "ID")
    private val formatter: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(localeID).apply {
            currency = Currency.getInstance("IDR")
            maximumFractionDigits = 0 // Rupiah tanpa desimal
        }
    }

    /** Format angka IDR langsung ke "Rp12.345". */
    fun rupiah(amount: Number): String = formatter.format(amount)

    /**
     * Terima Number/String (boleh ada simbol $, koma/titik), opsional konversi dari USD.
     * Contoh: rupiahFrom("$4.5", 16000.0) -> "Rp72.000"
     */
    fun rupiahFrom(value: Any?, usdToIdr: Double? = null): String {
        val number = when (value) {
            is Number -> value.toDouble()
            is String -> value
                .replace(Regex("[^0-9.,]"), "") // buang $, spasi, dsb.
                .replace(",", ".")
                .toDoubleOrNull() ?: 0.0
            else -> 0.0
        }
        val idr = usdToIdr?.let { number * it } ?: number
        return formatter.format(idr)
    }
}
