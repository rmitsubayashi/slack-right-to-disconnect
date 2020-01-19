package com.github.rmitsubayashi.presentation

import kotlinx.coroutines.CoroutineScope

interface BasePresenter: CoroutineScope {
    fun start()
    fun stop()
}