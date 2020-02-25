package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.OnboardingState
import com.github.rmitsubayashi.domain.repository.OnboardingRepository

class OnboardingInteractor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend fun shouldOnboard(): Resource<Boolean> {
        val resource = onboardingRepository.getOnboardingState()
        return when (resource.error) {
            null-> resource.data?.let { finished ->
                return Resource.success(!finished.isFinished)
            } ?: Resource.error()
            else -> Resource.error(resource.error)
        }
    }
    suspend fun finishOnboarding() {
        onboardingRepository.setOnboardingState(OnboardingState(true))
    }
}