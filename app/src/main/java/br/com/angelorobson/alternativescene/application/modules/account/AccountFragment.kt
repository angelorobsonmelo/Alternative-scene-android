package br.com.angelorobson.alternativescene.application.modules.account


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.CobreiApplication
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.databinding.AccountFragmentBinding
import kotlinx.android.synthetic.main.host_navigation_activity.*

class AccountFragment : FragmentBase() {

    private lateinit var binding: AccountFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        showBottomNavigation()
        showToolbar()
    }

    private fun showToolbar() {
        activity?.toolbar?.visibility = VISIBLE
        activity?.toolbar?.title = getString(R.string.settings)
    }

    private fun showBottomNavigation() {
        activity?.bottomNavigation?.visibility = VISIBLE
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.user = getUserLogged()
    }

    private fun getUserLogged() = CobreiApplication.mSessionUseCase.getAuthResponseInSession()?.user

}
