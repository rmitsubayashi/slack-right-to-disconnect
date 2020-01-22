package com.github.rmitsubayashi.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


class ConnectionManager(context: Context) {
    private val applicationContext = context.applicationContext

    fun isConnected(): Boolean {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting ?: false
    }
}