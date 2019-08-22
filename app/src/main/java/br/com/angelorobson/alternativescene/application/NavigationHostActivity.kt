package br.com.angelorobson.alternativescene.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import kotlinx.android.synthetic.main.host_navigation_activity.*


class NavigationHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_navigation_activity)

        val navController = Navigation.findNavController(this, R.id.my_nav_fragment)
        bottomNavigation?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragmentList = supportFragmentManager.fragments

        var handled = false
        for (f in fragmentList) {
            if (f is FragmentBase) {
                handled = f.onBackPressed()

                if (handled) {
                    break
                }
            }
        }

        if (!handled) {
            super.onBackPressed()
        }

        return handled
    }

}
