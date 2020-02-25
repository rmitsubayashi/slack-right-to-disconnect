package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.interactor.OnboardingInteractor
import com.github.rmitsubayashi.domain.interactor.SettingsInteractor
import com.github.rmitsubayashi.domain.model.SlackToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SlackTokenPresenter(
    private val view: SlackTokenContract.View,
    private val settingsInteractor: SettingsInteractor,
    private val onboardingInteractor: OnboardingInteractor
): SlackTokenContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
    }

    override fun stop() {
    }

    override fun verifyToken(input: String) {
        launch {
            val resource = settingsInteractor.saveSlackToken(SlackToken(input))
            withContext(Dispatchers.Main) {
                when (resource.error) {
                    null -> {
                        onboardingInteractor.finishOnboarding()
                        view.navigateToSlackTokenSuccess()
                    }
                    NetworkError.NOT_CONNECTED -> view.showNoNetwork()
                    else -> view.showValidationError()
                }
            }
        }

    }
}