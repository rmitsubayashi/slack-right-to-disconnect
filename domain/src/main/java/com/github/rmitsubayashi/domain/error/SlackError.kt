package com.github.rmitsubayashi.domain.error

enum class SlackError: IError {
    INVALID_SLACK_TOKEN,
    TOO_MANY_CHANNELS,
    TOO_MANY_USERS,
    RESTRICTED_CHANNEL
}