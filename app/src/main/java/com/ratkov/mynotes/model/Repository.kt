package com.ratkov.mynotes.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ratkov.mynotes.data.FireStoreProvider
import com.ratkov.mynotes.data.RemoteDataProvider
import java.util.*


class Repository(private val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()

    private val notesLiveData = MutableLiveData<List<Note>>()
    private val notes = mutableListOf<Note>()

    init {
        notesLiveData.value = notes
    }

    //    private fun addOrReplace(note: Note) {
//
//        for (i in 0 until notes.size) {
//            if (notes[i] == note) {
//                notes.set(i, note)
//                return
//            }
//        }
//
//        notes.add(note)
//    }
//
    fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
}
