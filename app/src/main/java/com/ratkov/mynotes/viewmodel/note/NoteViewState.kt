package com.ratkov.mynotes.viewmodel.note

import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.viewmodel.base.BaseViewState


class NoteViewState(data: Data = Data(), error: Throwable? = null)
    : BaseViewState<NoteViewState.Data>(data, error) {

    data class Data(
            val isDeleted: Boolean = false, val note: Note? = null)
}
