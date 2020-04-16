package com.ratkov.mynotes.viewmodel.main

import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.viewmodel.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null)
    : BaseViewState<List<Note>?>(notes, error)