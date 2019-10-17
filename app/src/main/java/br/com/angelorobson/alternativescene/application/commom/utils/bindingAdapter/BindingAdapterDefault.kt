package br.com.angelorobson.alternativescene.application.commom.utils.bindingAdapter

import android.graphics.BitmapFactory
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.*
import com.squareup.picasso.Picasso
import java.util.*


@BindingAdapter("date")
fun convertDateToString(textView: TextView, date: Date) {
    textView.text = date.convertDateToString()
}

@BindingAdapter("dateTime")
fun convertdateTimeToString(textView: TextView, date: Date) {
    textView.text = date.formatToServerDateTimeDefaults()
}

@BindingAdapter("initialDate")
fun convertInitialToString(textView: TextView, initialDate: Date) {
    textView.text = initialDate.convertDateToStringDDMMM()
}

@BindingAdapter("convertFormatToViewDateTimeDefaults")
fun convertFormatToViewDateTimeDefaults(textView: TextView, date: Date) {
    textView.text = date.formatToViewDateTimeDefaults()
}

@BindingAdapter("finalDate")
fun convertFinalDateToString(textView: TextView, finalDate: Date) {

    textView.text = finalDate.convertDateToStringDDMMM()
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("isFavorite")
fun isFavorite(view: ImageView, isFavorite: Boolean) {
    var imageResourceFavorite = R.drawable.ic_favorite_border_24dp

    if (isFavorite) {
        imageResourceFavorite = R.drawable.ic_favorite_fiiled_red_24dp
    }

    view.setImageResource(imageResourceFavorite)

}

@BindingAdapter("loadImageUrlOrBase64")
fun loadImageUrlOrBase64(view: ImageView, base64: String?) {
    if (base64 != null && base64.isNotEmpty()) {
        if (base64.startsWith("http")) {
            Picasso
                .get()
                .load(base64)
                .placeholder(R.drawable.heavy_metal_default)
                .into(view)
            return
        }

        view.setImageBitmap(base64.convertBase64ToBitmap())
    }
}

@BindingAdapter("loadImageFromPath")
fun loadImageFromPath(view: ImageView, path: String) {
    view.setImageBitmap(BitmapFactory.decodeFile(path))
}

@BindingAdapter("status")
fun status(imageView: ImageView, status: Boolean) {
    if (status) {
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                imageView.context,
                R.drawable.ic_check
            )
        )
    } else {
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                imageView.context,
                R.drawable.ic_error
            )
        )
    }
}


@BindingAdapter("visibleOrGone")
fun View.setVisibleOrGone(show: Boolean) {
    visibility = if (show) VISIBLE else GONE
}

@BindingAdapter("visible")
fun View.setVisible(show: Boolean) {
    visibility = if (show) VISIBLE else INVISIBLE
}