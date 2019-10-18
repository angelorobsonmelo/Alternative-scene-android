package br.com.angelorobson.alternativescene.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.DETAIL_EVENT_REQUEST_CODE
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.notEqual
import kotlinx.android.synthetic.main.host_navigation_activity.*


class NavigationHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_navigation_activity)

        val navController = Navigation.findNavController(this, R.id.my_nav_fragment)
        bottomNavigation?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.my_nav_fragment).navigateUp()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode.notEqual(DETAIL_EVENT_REQUEST_CODE)) {
            callActivityForResultMethodInAllFragments(requestCode, resultCode, data)
        }
    }

    private fun callActivityForResultMethodInAllFragments(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val fragmentList = supportFragmentManager.fragments
        for (f in fragmentList) {
            for (fragment in f.childFragmentManager.fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
