package com.mubarakansari.picture_in_picture_mode_kotlin_android

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    val time = Timer()
    var sec = 0
    private lateinit var task: TimerTask

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        task = object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                sec++
                this@MainActivity.runOnUiThread {
                    if (sec <= 10) {
                        if (sec > 9) {
                            textView.text = "00 :$sec"
                        } else {
                            textView.text = "00 :0$sec"
                        }
                    } else {
                        time.cancel()
                        time.purge()
                    }
                }
            }

        }


        textView.visibility = View.GONE

        btn_start.setOnClickListener {
            val mpipParams = PictureInPictureParams.Builder()
            val display = windowManager.defaultDisplay
            val point = Point()
            display.getSize(point)
            mpipParams.setAspectRatio(Rational(point.x, point.y))
            enterPictureInPictureMode(mpipParams.build())
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        if (isInPictureInPictureMode) {
            textView.visibility = View.VISIBLE
            btn_start.visibility = View.GONE
            sec = 0
            time.schedule(task, 0, 1000)
            supportActionBar?.hide()
        } else {
            textView.visibility = View.VISIBLE
            btn_start.visibility = View.VISIBLE
            supportActionBar?.show()
        }
    }
}