package com.example.nfc.di

import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.data.services.FireBaseAuthService
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
}
