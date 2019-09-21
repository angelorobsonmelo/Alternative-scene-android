package br.com.angelorobson.alternativescene.application.partials.events.eventimage


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.Constants
import br.com.angelorobson.alternativescene.application.commom.utils.FragmentBase
import br.com.angelorobson.alternativescene.domain.Event
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
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
        arguments?.let {
            mEvent = it.getParcelable(Constants.EventsContants.ARG_EVENT)
            convertImageUrlToBitMapAndShowOnScreen()
        }

        hideToolbar()
        hideBottomNavigation()

        linearLayoutClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun convertImageUrlToBitMapAndShowOnScreen() {
        Picasso.get().load(mEvent?.photoUrl).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                photo_view.setImageBitmap(bitmap)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
        })
    }


}
