package com.ratkov.mynotes.viewmodel.note

import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.viewmodel.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)