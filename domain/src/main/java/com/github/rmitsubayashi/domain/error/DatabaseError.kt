package com.github.rmitsubayashi.domain.error

enum class DatabaseError: IError {
    ALREADY_EXISTS,
    DOES_NOT_EXIST,
    COULD_NOT_SAVE
}