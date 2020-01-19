package com.github.rmitsubayashi.data.repository

import android.content.SharedPreferences

private const val breakSymbol = "$$"

fun SharedPreferences.Editor.putStringList(key: String, stringList: List<String>): SharedPreferences.Editor {
    val listString = stringList.joinToString(separator = breakSymbol)
    return this.putString(key, listString)
}

fun SharedPreferences.getStringList(key: String): List<String>? {
    val listString = this.getString(key, null) ?: return null
    return listString.split(breakSymbol)
}