package com.jeanpaulo.reignandroidtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeanpaulo.reignandroidtest.view.HitActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = generateIntent_MusicActivity()
        startActivity(intent)
        finish()
    }

    fun generateIntent_MusicActivity(): Intent {
        val intent = Intent(this, HitActivity::class.java)
        val b = Bundle()
        intent.putExtras(b)
        return intent
    }
}