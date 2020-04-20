package com.ratkov.mynotes.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.ratkov.mynotes.viewmodel.main.MainViewModel
import com.ratkov.mynotes.viewmodel.main.MainViewState
import com.ratkov.mynotes.R
import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.ui.base.BaseActivity
import com.ratkov.mynotes.ui.note.NoteActivity
import com.ratkov.mynotes.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?, MainViewState>(), LogoutDialog.LogoutListener {


    override val viewModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    override val layoutRes: Int = R.layout.activity_main
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        adapter = MainAdapter {note-> openNoteScreen(note)}
        mainRecycler.adapter = adapter

        fab.setOnClickListener{v -> openNoteScreen(null)}
    }


    companion object {
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()

                }
    }


    override fun renderData(data: List<Note>?) {
        if (data == null) return
        adapter.notes = data
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note?.id)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            MenuInflater(this).inflate(R.menu.menu_main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when(item.itemId) {
                R.id.logout -> showLogoutDialog().let { true }
                else -> false
            }


    private fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?:
        LogoutDialog.createInstance().show(supportFragmentManager, LogoutDialog.TAG)
    }



}


class LogoutDialog : DialogFragment() {
    companion object {
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.logout_dialog_title)
                    .setMessage(R.string.logout_dialog_message)
                    .setPositiveButton(R.string.ok_bth_title) { _, _ ->  (activity as LogoutListener).onLogout() }
                    .setNegativeButton(R.string.logout_dialog_cancel) {_, _ -> dismiss() }
                    .create()

    interface LogoutListener {
        fun onLogout()
    }
}
