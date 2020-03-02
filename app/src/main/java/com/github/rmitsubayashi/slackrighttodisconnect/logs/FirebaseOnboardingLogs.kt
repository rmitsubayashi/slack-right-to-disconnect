package com.github.rmitsubayashi.slackrighttodisconnect.logs

import com.github.rmitsubayashi.domain.logging.OnboardingLogs
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseOnboardingLogs(private val firebaseInstance: FirebaseAnalytics): OnboardingLogs {
    override fun openApp() {
        firebaseInstance.logEvent(OnboardingLogs.OPEN_APP, null)
    }

    override fun startSignIn() {
        firebaseInstance.logEvent(OnboardingLogs.START_SIGN_IN, null)
    }

    override fun completeSignIn() {
        firebaseInstance.logEvent(OnboardingLogs.COMPLETE_SIGN_IN, null)
    }


}