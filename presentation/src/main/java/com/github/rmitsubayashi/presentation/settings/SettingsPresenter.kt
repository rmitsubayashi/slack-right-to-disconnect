package com.github.rmitsubayashi.presentation.settings

import com.github.rmitsubayashi.domain.error.DatabaseError
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
        // load channels before checking current channel
        loadSlackChannels()
        checkCurrentSlackChannel()
        checkCurrentSlackToken()
    }

    override fun stop() {
        job.cancel()
    }

    override fun checkCurrentSlackChannel() {
        launch {
            val resultResource = settingsInteractor.getCurrentSlackChannel()
            withContext(Dispatchers.Main) {
                when (resultResource.error) {
                    null -> settingsView.updateSlackChannelSettingSummary(resultResource.data)
                    else -> settingsView.showInvalidSlackChannel()
                }
            }
        }
    }

    override fun checkCurrentSlackToken() {
        launch {
            val resultResource = settingsInteractor.getCurrentSlackTokenInfo()
            withContext(Dispatchers.Main) {
                settingsView.updateSlackTokenSettingSummary(resultResource.data)
            }
        }
    }

    override fun saveSlackChannel(slackChannelID: String) {
        launch {
            val resultResource = settingsInteractor.saveSlackChannel(slackChannelID)
            when (resultResource.error) {
                null -> {}
                DatabaseError.ALREADY_EXISTS -> { return@launch }
                else -> {
                    withContext(Dispatchers.Main) {
                        settingsView.showInvalidSlackChannel()
                    }
                    return@launch
                }
            }

            val slackChannel = settingsInteractor.findSlackChannelByID(slackChannelID)
            withContext(Dispatchers.Main) {
                if (slackChannel == null) {
                    settingsView.showInvalidSlackChannel()
                } else {
                    settingsView.updateSlackChannelSettingSummary(slackChannel)
                }
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
                        settingsView.updateSlackChannelSettingSummary(null)
                        loadSlackChannels()
                    }
                    DatabaseError.ALREADY_EXISTS -> {}
                    else -> settingsView.showInvalidSlackToken()
                }
            }
        }
    }

    override fun loadSlackChannels() {
        launch {
            val resultResource = settingsInteractor.getChannels()
            withContext(Dispatchers.Main) {
                settingsView.updateSlackChannelList(resultResource.data)
            }
        }
    }
}