package com.ratkov.mynotes.viewmodel.note

import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.model.Repository
import com.ratkov.mynotes.viewmodel.base.BaseViewModel
import com.ratkov.mynotes.model.NoteResult.Success
import com.ratkov.mynotes.model.NoteResult.Error



class NoteViewModel(val repository: Repository)
    : BaseViewModel<NoteViewState.Data, NoteViewState>(){

    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    override fun onCleared() {
        currentNote?.let { repository.saveNote(it) }
    }

    fun deleteNote() {
        currentNote?.let {
            repository.deleteNote(it.id).observeForever { t ->
                t?.let {
                    viewStateLiveData.value = when (it) {
                        is Success<*> -> NoteViewState(NoteViewState.Data(isDeleted = true))
                        is Error -> NoteViewState(error = it.error)
                    }
                }
            }
        }
    }

     fun loadNote(noteId: String) {
         repository.getNoteById(noteId).observeForever { t ->
             t?.let {
                 viewStateLiveData.value = when (t) {
                     is Success<*> -> NoteViewState(NoteViewState.Data(note = t.data as? Note))
                     is Error -> NoteViewState(error = t.error)
                 }
             }
         }
     }
 }