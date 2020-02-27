package com.github.rmitsubayashi.presentation.settings

import com.github.rmitsubayashi.domain.interactor.SettingsInteractor
import com.github.rmitsubayashi.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsPresenter(
    private val settingsView: SettingsContract.View,
    private val settingsInteractor: SettingsInteractor
): SettingsContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext = job + Dispatchers.Main

    override fun start() {
        checkCurrentSlackToken()
    }

    override fun stop() {
        job.cancel()
    }

    override fun checkCurrentSlackToken() {
        launch {
            val resultResource = settingsInteractor.getCurrentSlackTokenInfo()
            withContext(Dispatchers.Main) {
                settingsView.updateSlackTokenSettingSummary(resultResource.data)
            }
        }
    }

    override fun saveSlackToken(slackToken: SlackToken) {

    }
}