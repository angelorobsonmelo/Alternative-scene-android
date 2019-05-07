package br.com.soluevo.cobrei.application.modules.account


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.navigation.findNavController
import br.com.soluevo.cobrei.R
import br.com.soluevo.cobrei.application.commom.utils.FragmentBase
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.host_navigation_activity.*

class AccountFragment : FragmentBase() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.bottomNavigation?.visibility = VISIBLE
        activity?.toolbar?.visibility = VISIBLE
        activity?.toolbar?.title = "tudo certo"

    }
}
