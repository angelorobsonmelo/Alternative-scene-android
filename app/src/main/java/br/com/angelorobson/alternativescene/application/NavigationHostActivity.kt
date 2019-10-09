package br.com.angelorobson.alternativescene.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import br.com.angelorobson.alternativescene.R
import kotlinx.android.synthetic.main.host_navigation_activity.*


class NavigationHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_navigation_activity)

        val navController = Navigation.findNavController(this, R.id.my_nav_fragment)
        bottomNavigation?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragmentList = supportFragmentManager.fragments

        for (f in fragmentList) {
            for (fragment in f.childFragmentManager.fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }

    }
}
