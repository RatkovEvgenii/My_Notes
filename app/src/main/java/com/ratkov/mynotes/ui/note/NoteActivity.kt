package com.ratkov.mynotes.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProviders
import com.ratkov.mynotes.R
import com.ratkov.mynotes.format
import com.ratkov.mynotes.getColorInt
import com.ratkov.mynotes.model.Color
import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.ui.base.BaseActivity
import com.ratkov.mynotes.viewmodel.note.NoteViewModel
import com.ratkov.mynotes.viewmodel.note.NoteViewState
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.item_note.*
import java.util.*

private const val SAVE_DELAY = 2000L

@Suppress("DEPRECATION")
class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }
    override val viewModel: NoteViewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId?.let {
            viewModel.loadNote(it)
        }

        if (noteId == null ) supportActionBar?.title = getString(R.string.new_note_title)

        titleEt.doAfterTextChanged { triggerSaveNote() }
        bodyEt.doAfterTextChanged { triggerSaveNote()}
    }

    override fun renderData(data: Note?) {
        this.note = data
        initView()
    }

    private fun initView() {
        note?.run {
            supportActionBar?.title = lastChanged.format()

            titleEt.setText(title)
            bodyEt.setText(note)

            toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun triggerSaveNote() {
        if (titleEt.text?.length?:0 < 3) return

        Handler().postDelayed(object : Runnable {
            override fun run() {
                note = note?.copy(title = titleEt.text.toString(),
                    note = bodyEt.text.toString(),
                    lastChanged = Date())
                    ?: createNewNote()

                if (note != null) viewModel.saveChanges(note!!)
            }

        }, SAVE_DELAY)
    }

    private fun createNewNote(): Note = Note(UUID.randomUUID().toString(),
        titleEt.text.toString(),
        bodyEt.text.toString())
}
