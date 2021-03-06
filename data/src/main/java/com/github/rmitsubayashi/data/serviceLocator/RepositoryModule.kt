package com.github.rmitsubayashi.data.serviceLocator

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.github.rmitsubayashi.data.BuildConfig
import com.github.rmitsubayashi.data.local.sharedpreferences.SecureSharedPrefKeys
import com.github.rmitsubayashi.data.local.sharedpreferences.SharedPrefsKeys
import com.github.rmitsubayashi.data.local.sqlite.AppDatabase
import com.github.rmitsubayashi.data.repository.*
import com.github.rmitsubayashi.data.service.SlackService
import com.github.rmitsubayashi.data.util.ConnectionManager
import com.github.rmitsubayashi.domain.repository.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule = module {
    single<SlackAuthenticationRepository> {
        SlackAuthenticationDataRepository(
            get(named("securePrefs")),
            get(named("normalPrefs")),
            get(),
            get()
        )
    }

    single<SlackMessageRepository> {
        SlackMessageDataRepository(
            get(),
            get(),
            get<AppDatabase>().threadDao(),
            get()
        )
    }

    single<SlackTeamRepository> {
        SlackTeamDataRepository(
            get(),
            get(),
            get()
        )
    }

    single<OnboardingRepository> {
        OnboardingDataRepository(get(named("normalPrefs")))
    }

    single<SlackService> {
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://slack.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
        retrofit.create(SlackService::class.java)
    }

    single<BookmarkRepository> {
        BookmarkDataRepository(get<AppDatabase>().bookmarkDao())
    }

    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, AppDatabase.DB_NAME)
            .build()
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

    factory {
        ConnectionManager(androidContext())
    }
}