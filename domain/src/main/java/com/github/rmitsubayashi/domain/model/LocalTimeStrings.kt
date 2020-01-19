package com.github.rmitsubayashi.domain.model

import java.util.*

object LocalTimeStrings {
    fun pluralMinuteString(locale: Locale): String =
        when (locale) {
            Locale.JAPAN -> "分"
            else -> "mins"
        }

    fun pluralHourString(locale: Locale): String =
        when (locale) {
            Locale.JAPAN -> "時間"
            else -> "hrs"
        }

    fun singularMinuteString(locale: Locale): String =
        when(locale) {
            Locale.JAPAN -> "分"
            else -> "min"
        }

    fun singularHourString(locale: Locale): String =
        when (locale) {
            Locale.JAPAN -> "時間"
            else -> "hr"
        }
}