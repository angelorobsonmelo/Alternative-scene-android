package br.com.angelorobson.alternativescene.application.partials.account


import android.content.DialogInterface
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.listeners.dialog.ListenerConfirmDialog
import br.com.angelorobson.alternativescene.databinding.AccountFragmentBinding


class AccountFragment : BindingFragment<AccountFragmentBinding>() {

    override fun getLayoutResId(): Int = R.layout.account_fragment

    private val mSessionUseCase =
        AlternativeSceneApplication.mSessionUseCase

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpElements()
    }

    private fun setUpElements() {
        showToolbarWithDisplayArrowBack(getString(R.string.account))
        hideBottomNavigation()
        setUserInUi()
        initLogoutListener()
    }

    private fun setUserInUi() {
        if (mSessionUseCase.isLogged()) {
            val userApp = mSessionUseCase.getAuthResponseInSession()?.userAppDto
            binding.user = userApp
        }
    }

    private fun initLogoutListener() {
        binding.logoutTextView.setOnClickListener {
            showConfirmDialog(
                "",
                R.string.are_you_logout.toString(),
                object : ListenerConfirmDialog {
                    override fun onPressPositiveButton(dialog: DialogInterface, id: Int) {
                        signOutGoogle()
                        signOutFireBase()
                        mSessionUseCase.destroySession()
                        findNavController().popBackStack()
                    }

                    override fun onPressNegativeButton(dialog: DialogInterface, id: Int) {

                    }

                })
        }
    }


}
