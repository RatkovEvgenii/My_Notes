package com.ratkov.mynotes.ui.note

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.ratkov.mynotes.R
import com.ratkov.mynotes.format
import com.ratkov.mynotes.getColorInt
import com.ratkov.mynotes.model.Color
import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.ui.base.BaseActivity
import com.ratkov.mynotes.ui.main.LogoutDialog
import com.ratkov.mynotes.viewmodel.note.NoteViewModel
import com.ratkov.mynotes.viewmodel.note.NoteViewState
import com.ratkov.mynotes.viewmodel.note.NoteViewState.Data
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel

import java.util.*

private const val SAVE_DELAY = 2000L


class DeleteDialog : DialogFragment() {
    companion object {
        val TAG = DeleteDialog::class.java.name + "TAG"
        fun createInstance() = DeleteDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.delete_dialog_title)
                    .setMessage(R.string.delete_dialog_message)
                    .setPositiveButton(R.string.ok_bth_title) { _, _ -> (activity as DeleteListener).onDelete() }
                    .setNegativeButton(R.string.logout_dialog_cancel) { _, _ -> dismiss() }
                    .create()

    interface DeleteListener {
        fun onDelete()
    }
}

@Suppress("DEPRECATION")
class NoteActivity : BaseActivity<Data, NoteViewState>(),DeleteDialog.DeleteListener {
    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private val TAG_DELETE = "DeleteDialog TAG"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    override val viewModel: NoteViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null
    private var textWatcher: TextWatcher? = null
    private var color: Color = Color.YELLOW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId?.let {
            viewModel.loadNote(it)
        }

        if (noteId == null) supportActionBar?.title = getString(R.string.new_note_title)

        titleEt.doAfterTextChanged { triggerSaveNote() }
        bodyEt.doAfterTextChanged { triggerSaveNote() }

        colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            triggerSaveNote()
        }
    }

    override fun renderData(data: Data) {
        if (data.isDeleted) finish()
        if(data.note?.color == note?.color &&
                data.note?.title == note?.title &&
                data.note?.note == note?.note) return
        this.note = data.note
        data.note?.let { color = it.color }
        initView()
    }

    private fun initView() {

        note?.run {
            supportActionBar?.title = lastChanged.format()
            toolbar.setBackgroundColor(
                    color.getColorInt(this@NoteActivity)
            )

            removeEditListener()
            titleEt.setText(title)
            bodyEt.setText(note)
            setEditListener()
        }
    }

    private fun setEditListener() {
        textWatcher = titleEt.doAfterTextChanged { triggerSaveNote() }
        bodyEt.addTextChangedListener(textWatcher);
    }

    private fun removeEditListener() {
        titleEt.removeTextChangedListener(textWatcher)
        bodyEt.removeTextChangedListener(textWatcher)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun triggerSaveNote() {
        if (titleEt.text?.length ?: 0 < 3 || bodyEt.text.length < 3) return

        Handler().postDelayed({
            note = note?.copy(
                    title = titleEt.text.toString(),
                    note = bodyEt.text.toString(),
                    lastChanged = Date(),
                    color = color
            )
                    ?: createNewNote()

            note?.let { viewModel.saveChanges(it) }
        }, SAVE_DELAY)
    }

    private fun createNewNote(): Note = Note(
            UUID.randomUUID().toString(),
            titleEt.text.toString(),
            bodyEt.text.toString()
    )

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            menuInflater.inflate(R.menu.note_menu, menu).let { true }

    private fun deleteNote() {
        supportFragmentManager.findFragmentByTag(DeleteDialog.TAG) ?:
        DeleteDialog.createInstance().show(supportFragmentManager, DeleteDialog.TAG)
    }

    override fun onDelete() {
        viewModel.deleteNote()
    }


    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            return
        }
        super.onBackPressed()
    }

    private fun setToolbarColor(color: Color) {
        toolbar.setBackgroundColor(color.getColorInt(this))
    }
}
