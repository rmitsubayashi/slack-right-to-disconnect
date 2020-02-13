package com.github.rmitsubayashi.presentation.settings

import com.github.rmitsubayashi.domain.error.DatabaseError
import com.github.rmitsubayashi.domain.error.SlackError
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
        launch {
            val resultResource = settingsInteractor.saveSlackToken(slackToken)
            withContext(Dispatchers.Main) {
                when (resultResource.error) {
                    null -> {
                        settingsView.updateSlackTokenSettingSummary(resultResource.data)
                        settingsView.showSaved()
                    }
                    DatabaseError.ALREADY_EXISTS -> settingsView.showSaved()
                    SlackError.INVALID_SLACK_TOKEN -> settingsView.showInvalidSlackToken()
                    else -> settingsView.showGeneralError()
                }
            }
        }
    }
}