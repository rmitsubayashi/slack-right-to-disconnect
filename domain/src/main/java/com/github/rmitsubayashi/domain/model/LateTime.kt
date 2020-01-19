package com.github.rmitsubayashi.domain.model

import java.util.*

data class LateTime(
    val hours: Int,
    val minutes: Int
) {
    override fun toString(): String {
        val locale = Locale.getDefault()
        var text = ""
        when (hours) {
            1 -> text += "$hours ${LocalTimeStrings.singularHourString(locale)}"
            0 -> {}
            else -> text += "$hours ${LocalTimeStrings.pluralHourString(locale)}"
        }

        if (minutes > 0) {
            if (text.isNotEmpty()) {
                text += " "
            }
            text += if (minutes == 0 || minutes == 1) {
                "$minutes ${LocalTimeStrings.singularMinuteString(locale)}"
            } else {
                "$minutes ${LocalTimeStrings.pluralMinuteString(locale)}"
            }
        }
        return text
    }
}
