package com.example.nfc.di

import android.content.SharedPreferences
import com.example.nfc.data.preference.CommonPreference
import com.example.nfc.data.preference.Preference
import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.data.services.FireBaseAuthService
import com.example.nfc.data.services.PhotoManager
import com.example.nfc.data.services.PhotoManagerService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindFireBaseAuth(
        fireBaseAuth: FireBaseAuth
    ): FireBaseAuthService

    @Binds
    @Singleton
    abstract fun bindPhotoManager(
        photoManager: PhotoManager
    ): PhotoManagerService
    @Binds
    @Singleton
    abstract fun bindPreferences(
        preferences: CommonPreference
    ): Preference
}
