package com.ratkov.mynotes.viewmodel.spalsh

import com.ratkov.mynotes.viewmodel.base.BaseViewState


class SplashViewState(isAuth: Boolean? = null, error: Throwable? = null) :
        BaseViewState<Boolean?>(isAuth, error)
