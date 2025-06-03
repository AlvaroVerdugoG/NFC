package com.example.nfc.data

import android.content.Context
import com.example.nfc.data.preference.CommonPreference
import com.example.nfc.data.preference.Preference
import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.data.services.FireBaseAuthService
import com.example.nfc.data.services.FireBaseStorage
import com.example.nfc.data.services.FireBaseStorageService
import com.example.nfc.data.services.PhotoManager
import com.example.nfc.data.services.PhotoManagerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun providePhotoManger(@ApplicationContext context: Context): PhotoManagerService =
        PhotoManager(context)
    @Singleton
    @Provides
    fun provideFireBaseAuth(@ApplicationContext context: Context): FireBaseAuthService =
        FireBaseAuth(context)
    @Singleton
    @Provides
    fun provideFireBaseStorage(@ApplicationContext context: Context): FireBaseStorageService =
        FireBaseStorage(context)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)!!
}
