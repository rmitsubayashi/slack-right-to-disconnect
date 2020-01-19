package com.github.rmitsubayashi.data.serviceLocator

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.github.rmitsubayashi.data.repository.MessageDataRepository
import com.github.rmitsubayashi.data.repository.SecureSharedPrefKeys
import com.github.rmitsubayashi.data.repository.SharedPrefsKeys
import com.github.rmitsubayashi.data.repository.SlackDataRepository
import com.github.rmitsubayashi.data.service.SlackService
import com.github.rmitsubayashi.domain.repository.MessageRepository
import com.github.rmitsubayashi.domain.repository.SlackRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule = module {
    single<MessageRepository> {
        MessageDataRepository(get(named("normalPrefs")))
    }
    single<SlackRepository> {
        SlackDataRepository(get(named("securePrefs")), get(named("normalPrefs")), get())
    }

    single<SlackService> {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://slack.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(SlackService::class.java)
    }


    factory(named("securePrefs")) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        EncryptedSharedPreferences
            .create(
                SecureSharedPrefKeys.FILE_NAME,
                masterKeyAlias,
                androidContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }

    factory(named("normalPrefs")) {
        androidApplication().getSharedPreferences(SharedPrefsKeys.FILE_NAME, Context.MODE_PRIVATE)
    }
}