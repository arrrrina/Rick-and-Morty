package com.example.project1


import android.app.Application
import com.example.project1.common.di.module.appModule
import com.example.project1.common.di.module.databaseModule
import com.example.project1.common.di.module.repositoryModule
import com.example.project1.common.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.slf4j.event.Level

class CharacterApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CharacterApp)
            modules(listOf(viewModelModule, databaseModule, repositoryModule, appModule))
        }
    }
}