package com.github.rmitsubayashi.domain.model

inline class MessageTemplate(val value: String) {
    companion object {
        const val LATE_TIME_PLACEHOLDER = "==late_time=="
    }

    // the placeholders can be null or empty
    internal fun createMessage(lateTime: LateTime?): Message {
        val replaceLateTime =
            lateTime?.let { value.replace(LATE_TIME_PLACEHOLDER, it.toString()) }
                ?: value
        return Message(replaceLateTime)
    }

    fun hasLateTimePlaceHolder() = value.contains(LATE_TIME_PLACEHOLDER)
}