package com.ratkov.mynotes.viewmodel.spalsh

import com.ratkov.mynotes.data.errors.NoAuthException
import com.ratkov.mynotes.model.Repository
import com.ratkov.mynotes.viewmodel.base.BaseViewModel


class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(isAuth = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}