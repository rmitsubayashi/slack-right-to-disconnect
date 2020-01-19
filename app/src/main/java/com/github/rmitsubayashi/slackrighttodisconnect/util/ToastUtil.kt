package com.github.rmitsubayashi.slackrighttodisconnect.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(resID: Int) {
    val length = if (getString(resID).length > 10) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, resID, length).show()
}

fun Context.showToast(text: String) {
    val length = if (text.length > 10) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, text, length).show()
}