package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.SlackToken
import com.github.rmitsubayashi.domain.model.SlackTokenInfo
import com.github.rmitsubayashi.domain.repository.SlackAuthenticationRepository

class AuthenticationInteractor(
    private val slackAuthenticationRepository: SlackAuthenticationRepository,
    private val onboardingInteractor: OnboardingInteractor
) {
    fun getSlackLoginURL(): String {
        val permissions = arrayListOf("chat:write:user","channels:read","users:read","groups:write","im:write","mpim:write")
        val permissionsString = permissions.joinToString(",")
        return "https://slack.com/oauth/authorize?scope=${permissionsString}&client_id=311041719362.971614530550&redirect_uri=https%3A%2F%2Fslack-right-to-disconnect.com%2Fauthenticate&state=aaa"
    }

    suspend fun exchangeCodeForAccessToken(code: String, state: String): Resource<Unit> {
        val tokenResource = slackAuthenticationRepository.generateSlackToken(code)
        if (tokenResource.error != null) {
            return Resource.error(tokenResource.error)
        }
        if (tokenResource.data == null) {
            return Resource.error()
        }

        slackAuthenticationRepository.setSlackToken(SlackTokenInfo(SlackToken(tokenResource.data), "",""))
        onboardingInteractor.finishOnboarding()
        return Resource.success()
        /*val validationResource = slackAuthenticationRepository.validateSlackToken(SlackToken(tokenResource.data))
        when (validationResource.error) {
            null -> {
                validationResource.data?.let {
                    slackAuthenticationRepository.setSlackToken(it)
                    return Resource.success()
                } ?: return Resource.error()
            }
            else -> return Resource.error(validationResource.error)
        }*/
    }
}