package br.com.angelorobson.alternativescene.application

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.DETAIL_EVENT_REQUEST_CODE
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.notEqual
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.android.synthetic.main.host_navigation_activity.*
import java.text.MessageFormat


class NavigationHostActivity : AppCompatActivity() {

    private lateinit var mBottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_navigation_activity)

        val navController = Navigation.findNavController(this, R.id.my_nav_fragment)
        bottomNavigation?.setupWithNavController(navController)

        mBottomNavigationView = bottomNavigation


        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...

                // ...
            }
            .addOnFailureListener(this) { e -> Log.w("TAG", "getDynamicLink:onFailure", e) }

        val id = 166
        val firebaseLink = "https://angelorobsonn.page.link"
        val myLink = "https://www.angelorobson.com?id=$id"
        val myPackage = "br.com.angelorobson.alternativescene"
        val link = "{0}?link={1}&apn={2}"
    }

    private fun shareDeepLink(deepLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link")
        intent.putExtra(Intent.EXTRA_TEXT, deepLink)

        startActivity(intent)
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

    fun clickOnEventMenu() {
        clickNavigationItemProgrammaticallyById(R.id.eventFormFragment)
    }

    private fun clickNavigationItemProgrammaticallyById(nav_id: Int) {
        val view = mBottomNavigationView.findViewById<View>(nav_id)
        view.performClick()
    }
}
