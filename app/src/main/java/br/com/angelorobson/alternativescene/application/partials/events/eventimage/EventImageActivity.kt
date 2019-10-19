package br.com.angelorobson.alternativescene.application.partials.events.eventimage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventImageConstants.EVENT_IMAGE_URL_EXTRA
import br.com.angelorobson.alternativescene.application.commom.utils.Constants.EventImageConstants.HTTP
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.convertBase64ToBitmap
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_image_activity.*

class EventImageActivity : AppCompatActivity() {

    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_image_activity)

        setUpElements()
    }

    private fun setUpElements() {
        setImagePreviewFromArguments()
        handleClickListener()
    }

    private fun setImagePreviewFromArguments() {
        intent?.apply {
           handleEventPreview()
        }
    }

    private fun Intent.handleEventPreview() {
        imageUrl = getStringExtra(EVENT_IMAGE_URL_EXTRA)
        if (imageUrl.isNotEmpty()) {
            when {
                imageUrl.startsWith(HTTP) -> {
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.heavy_metal_default)
                        .into(photo_view)
                }
                else -> {
                    val imageBase64 = imageUrl.convertBase64ToBitmap()
                    photo_view.setImageBitmap(imageBase64)
                }
            }
        }
    }

    private fun handleClickListener() {
        linearLayoutClose.setOnClickListener {
            finish()
        }

        linearLayoutClose.setOnClickListener {
            finish()
        }
    }
}
