package br.com.angelorobson.alternativescene.application.partials.events.favorite


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.isEqual
import br.com.angelorobson.alternativescene.application.commom.utils.googlemanager.GoogleManager
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity
import br.com.angelorobson.alternativescene.application.partials.signin.SignInActivity.Companion.GOOGLE_AUTH_REQUEST_CODE
import br.com.angelorobson.alternativescene.databinding.FavoriteFragmentBinding
import javax.inject.Inject


class FavoriteFragment : BindingFragment<FavoriteFragmentBinding>() {

    override fun getLayoutResId(): Int = R.layout.favorite_fragment

    @Inject
    lateinit var mLayoutManager: LinearLayoutManager

    @Inject
    lateinit var mRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIfUserIsLogged()
    }

    private fun checkIfUserIsLogged() {
        val isUserLogged =
            AlternativeSceneApplication.mSessionUseCase.isLogged()

        if (isUserLogged) {
            return
        }

        goToSignInActivity()
    }

    private fun goToSignInActivity() {
        startActivityForResult(
            Intent(requireContext(), SignInActivity::class.java),
            GOOGLE_AUTH_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (isSuccess(requestCode, resultCode)) {
            print("ffff")
        }

    }

    private fun isSuccess(requestCode: Int, resultCode: Int): Boolean {
        return requestCode.isEqual(GOOGLE_AUTH_REQUEST_CODE) && resultCode.isEqual(
            Activity.RESULT_OK
        )
    }

}
