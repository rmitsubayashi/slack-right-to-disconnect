package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.OnboardingState

interface OnboardingRepository {
    suspend fun getOnboardingState(): Resource<OnboardingState>
    suspend fun setOnboardingState(onboardingState: OnboardingState): Resource<Unit>
}