package br.com.angelorobson.alternativescene.application.commom.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class ActivityBase: AppCompatActivity() {


     fun showProgressBarWithFragNotTouchable(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
         window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideProgressBarWithFragNotTouchable(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE;
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}