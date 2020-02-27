package com.github.rmitsubayashi.data.service.model.request

import android.util.Base64

class SlackClientCredentials(private val clientID: String, private val clientSecret: String) {
    fun generateEncodedString(): String {
        val credentials = "${clientID}:${clientSecret}"
        val bytes = credentials.toByteArray(Charsets.UTF_8)
        val encodedCredentials= Base64.encodeToString(bytes, Base64.NO_WRAP)
        return "Basic $encodedCredentials"
    }
}