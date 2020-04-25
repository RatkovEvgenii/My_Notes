package com.ratkov.mynotes.ui.spalsh

import android.os.Handler
import com.ratkov.mynotes.R
import com.ratkov.mynotes.ui.base.BaseActivity
import com.ratkov.mynotes.ui.main.MainActivity
import com.ratkov.mynotes.viewmodel.spalsh.SplashViewModel
import com.ratkov.mynotes.viewmodel.spalsh.SplashViewState
import org.koin.android.viewmodel.ext.android.viewModel
//import com.ratkov.mynotes.ui.note.NoteActivity.Companion.getStartIntent


private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int = com.ratkov.mynotes.R.layout.activity_splash

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf{ it }?.let {
            startMainActivity()
        }
    }


    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}
