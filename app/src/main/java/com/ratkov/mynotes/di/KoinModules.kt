package com.ratkov.mynotes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ratkov.mynotes.data.FireStoreProvider
import com.ratkov.mynotes.data.RemoteDataProvider
import com.ratkov.mynotes.model.Repository
import com.ratkov.mynotes.viewmodel.main.MainViewModel
import com.ratkov.mynotes.viewmodel.note.NoteViewModel
import com.ratkov.mynotes.viewmodel.spalsh.SplashViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}