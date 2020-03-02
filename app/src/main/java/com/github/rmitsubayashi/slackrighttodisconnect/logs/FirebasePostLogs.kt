package com.github.rmitsubayashi.slackrighttodisconnect.logs

import com.github.rmitsubayashi.domain.logging.PostLogs
import com.google.firebase.analytics.FirebaseAnalytics

class FirebasePostLogs(private val firebaseInstance: FirebaseAnalytics): PostLogs {
    override fun postMessage() {
        firebaseInstance.logEvent(PostLogs.POST_MESSAGE, null)
    }
}