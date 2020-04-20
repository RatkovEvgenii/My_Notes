package com.ratkov.mynotes.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.ratkov.mynotes.data.errors.NoAuthException
import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.model.NoteResult
import com.ratkov.mynotes.model.User

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider : RemoteDataProvider {

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    getUserNotesCollection().addSnapshotListener { snapshot, e ->
                        value = e?.let { throw it }
                                ?: snapshot?.let {
                                    val notes = it.documents.map { it.toObject(Note::class.java) }
                                    NoteResult.Success(notes)
                                }
                    }
                }catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }


    override fun saveNote(note: Note): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    getUserNotesCollection().document(note.id)
                            .set(note).addOnSuccessListener {
                                Log.d(TAG, "Note $note is saved")
                                value = NoteResult.Success(note)
                            }.addOnFailureListener {
                                Log.d(TAG, "Error saving note $note, message: ${it.message}")
                                throw it
                            }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }


    override fun getCurrentUser(): LiveData<User?> =
            MutableLiveData<User?>().apply {
                value = currentUser?.let { User(it.displayName ?: "",
                        it.email ?: "") }
            }



    override fun getNoteById(id: String): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {

                    getUserNotesCollection().document(id).get()
                            .addOnSuccessListener {
                                value = NoteResult.Success(it.toObject(Note::class.java))
                            }.addOnFailureListener {
                                throw it
                            }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }


}
