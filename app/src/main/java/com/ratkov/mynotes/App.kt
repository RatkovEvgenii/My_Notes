package com.ratkov.mynotes

import androidx.multidex.MultiDexApplication
import com.ratkov.mynotes.di.appModule
import com.ratkov.mynotes.di.mainModule
import com.ratkov.mynotes.di.noteModule
import com.ratkov.mynotes.di.splashModule
import org.koin.android.ext.android.startKoin


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}