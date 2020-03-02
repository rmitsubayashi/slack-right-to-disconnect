package com.github.rmitsubayashi.domain.logging

interface OnboardingLogs {
    fun openApp()
    fun startSignIn()
    fun completeSignIn()
    companion object {
        const val OPEN_APP = "open_app"
        const val START_SIGN_IN = "start_sign_in"
        const val COMPLETE_SIGN_IN = "complete_sign_in"
    }
}