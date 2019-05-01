package br.com.soluevo.cobrei.application.modules.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import br.com.soluevo.cobrei.R
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goToAccountBtn.setOnClickListener ( Navigation.createNavigateOnClickListener(R.id.accountFragment))
        goToSettingsBtn.setOnClickListener {
            it.findNavController().navigate(R.id.toSettings)
        }
    }
}
