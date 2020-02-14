package com.github.rmitsubayashi.domain.model

// might need fixing? https://developer.android.com/guide/navigation/navigation-pass-data?hl=en#use_keep_annotations
enum class RecipientType(val id: Int) {
    CHANNEL(1), USER(2), THREAD(3);

    companion object {
        fun valueOf(id: Int): RecipientType {
            return when (id) {
                CHANNEL.id -> CHANNEL
                USER.id -> USER
                THREAD.id -> THREAD
                else -> throw ClassCastException()
            }
        }
    }
}