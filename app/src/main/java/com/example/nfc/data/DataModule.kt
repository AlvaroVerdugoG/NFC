package com.example.nfc.data

import android.content.Context
import com.example.nfc.data.preference.CommonPreference
import com.example.nfc.data.preference.Preference
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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)!!
}
