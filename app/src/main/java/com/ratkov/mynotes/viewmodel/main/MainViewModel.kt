package com.ratkov.mynotes.viewmodel.main

import androidx.lifecycle.Observer
import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.model.NoteResult
import com.ratkov.mynotes.model.Repository
import com.ratkov.mynotes.viewmodel.base.BaseViewModel
import com.ratkov.mynotes.model.NoteResult.Success
import com.ratkov.mynotes.model.NoteResult.Error

class MainViewModel(val repository: Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult> {//Стандартный обсервер LiveData
    override fun onChanged(t: NoteResult?) {
        if (t == null) return

        when(t) {
            is Success<*> -> {
// Может понадобиться вручную импортировать класс data.model.NoteResult.Success
                viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
            }
            is Error -> {
// Может понадобиться вручную импортировать класс data.model.NoteResult.Error
                viewStateLiveData.value = MainViewState(error = t.error)
            }
        }
    }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}