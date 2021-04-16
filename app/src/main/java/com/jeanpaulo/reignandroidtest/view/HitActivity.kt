package com.jeanpaulo.reignandroidtest.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.jeanpaulo.reignandroidtest.R
import com.jeanpaulo.reignandroidtest.view.fragments.HitListFragmentListener


class HitActivity : AppCompatActivity(),
    HitListFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hit)
    }
}