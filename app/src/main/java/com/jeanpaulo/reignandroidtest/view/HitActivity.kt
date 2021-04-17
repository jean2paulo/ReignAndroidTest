package com.jeanpaulo.reignandroidtest.view

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jeanpaulo.reignandroidtest.R
import com.jeanpaulo.reignandroidtest.view.fragments.HitDetailFragmentListener
import com.jeanpaulo.reignandroidtest.view.fragments.HitListFragmentListener


class HitActivity : AppCompatActivity(),
    HitListFragmentListener,
    HitDetailFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hit)

        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            supportActionBar!!.title = navController.currentDestination?.label
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        //Removed super.onCreateContextMenu to work with context on fragment
        //REF: https://stackoverflow.com/questions/20825118/inappropriate-context-menu-within-a-fragment
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() ||
                super.onSupportNavigateUp()
    }

    override fun setTitle(title: String) {
        supportActionBar!!.title = title
    }

    override fun setHomeUpAsEnable(boolean: Boolean) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(boolean)
    }
}