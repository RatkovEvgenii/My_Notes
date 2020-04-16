package com.ratkov.mynotes.data

import androidx.lifecycle.LiveData
import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.model.NoteResult

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>
}
