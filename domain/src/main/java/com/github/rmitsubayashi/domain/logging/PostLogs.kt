package com.github.rmitsubayashi.domain.logging

interface PostLogs {
    fun postMessage()

    companion object {
        const val POST_MESSAGE = "post_message"
    }
}