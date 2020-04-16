package com.ratkov.mynotes.viewmodel.note

import androidx.lifecycle.Observer
import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.model.NoteResult
import com.ratkov.mynotes.model.Repository
import com.ratkov.mynotes.viewmodel.base.BaseViewModel
import com.ratkov.mynotes.model.NoteResult.Success
import com.ratkov.mynotes.model.NoteResult.Error


class NoteViewModel(val repository: Repository = Repository) : BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                if (t == null) return

                when (t) {
                    is Success<*> ->
                        viewStateLiveData.value = NoteViewState(note = t.data as? Note)
                    is Error ->
                        viewStateLiveData.value = NoteViewState(error = t.error)
                }
            }
        })
    }
}