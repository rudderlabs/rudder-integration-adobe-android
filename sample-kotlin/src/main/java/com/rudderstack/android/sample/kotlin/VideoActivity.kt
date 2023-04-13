package com.rudderstack.android.sample.kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rudderlabs.android.sample.kotlin.R

class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val navigateToMainActivity = findViewById<Button>(R.id.navigateToMainActivity)
        navigateToMainActivity?.setOnClickListener()
        {
            // displaying a toast message
            val i = Intent(this@VideoActivity, MainActivity::class.java)
            startActivity(i)
        }
    }

}