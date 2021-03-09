package ir.rezarasuolzadeh.takhfif.service.utils

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_toolbar.*

object CustomToolbar {

    fun setSimpleToolbar(
        activity: AppCompatActivity,
        title: String
    ) {
        activity.titleTextView.text = title
        activity.backButton.setOnClickListener {
            activity.onBackPressed()
        }
        activity.backButton.visibility = View.VISIBLE
    }

}