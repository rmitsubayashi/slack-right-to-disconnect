package com.github.rmitsubayashi.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.rmitsubayashi.data.local.sharedpreferences.SharedPrefsKeys
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.OnboardingState
import com.github.rmitsubayashi.domain.repository.OnboardingRepository

class OnboardingDataRepository(
    private val sharedPreferences: SharedPreferences
): OnboardingRepository {
    override suspend fun getOnboardingState(): Resource<OnboardingState> {
        val finished = sharedPreferences.getBoolean(SharedPrefsKeys.ONBOARDING_FINISHED, false)
        return Resource.success(OnboardingState(finished))
    }

    override suspend fun setOnboardingState(onboardingState: OnboardingState): Resource<Unit> {
        sharedPreferences.edit {
            putBoolean(SharedPrefsKeys.ONBOARDING_FINISHED, onboardingState.isFinished)
        }
        return Resource.success()
    }
}