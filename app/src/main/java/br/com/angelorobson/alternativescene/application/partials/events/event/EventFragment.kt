package br.com.angelorobson.alternativescene.application.partials.events.event


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.BindingFragment
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventsContants.ARG_EVENT
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.databinding.EventFragmentBinding
import br.com.angelorobson.alternativescene.domain.Event


class EventFragment : BindingFragment<EventFragmentBinding>() {

    override fun getLayoutResId(): Int = R.layout.event_fragment

    private var mEvent: Event? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            mEvent = it.getParcelable<Event>(ARG_EVENT)
        }

        setUpElements()
    }

    private fun setUpElements() {
        setUpBinding()
        hideBottomNavigation()
        showToolbarWithDisplayArrowBack(mEvent?.title ?: "")
    }

    private fun setUpBinding() {
        binding.lifecycleOwner = this
    }


}
