package com.github.rmitsubayashi.domain.error

enum class ValidationError: IError {
    INVALID_SLACK_TOKEN,
    INVALID_SLACK_CHANNEL_ID,
    EMPTY_LATE_TIME
}