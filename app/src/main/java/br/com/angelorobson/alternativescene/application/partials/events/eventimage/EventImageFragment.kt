package br.com.angelorobson.alternativescene.application.partials.events.eventimage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.Constants
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.convertBase64ToBitmap
import br.com.angelorobson.alternativescene.domain.Event
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_image_fragment.*


class EventImageFragment : FragmentBase() {

    private var mEvent: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpElements()
    }

    private fun setUpElements() {
        setImagePreviewFromArguments()
        hideToolbar()
        hideBottomNavigation()
        handleClickListener()
    }

    private fun setImagePreviewFromArguments() {
        arguments?.let {
            mEvent = it.getParcelable(Constants.EventsContants.ARG_EVENT)

            if (mEvent?.imageUrl?.startsWith("http")!!) {
                Picasso.get()
                    .load(mEvent?.imageUrl)
                    .into(photo_view)
                return
            }

            val imageBase64 = mEvent?.imageUrl?.convertBase64ToBitmap()
            photo_view.setImageBitmap(imageBase64)

        }
    }

    private fun handleClickListener() {
        linearLayoutClose.setOnClickListener {
            findNavController().popBackStack()
        }

        linearLayoutClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}
