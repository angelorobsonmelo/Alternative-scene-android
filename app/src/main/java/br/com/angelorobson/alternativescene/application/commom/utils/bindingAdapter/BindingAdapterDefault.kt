package br.com.angelorobson.alternativescene.application.commom.utils.bindingAdapter

import android.graphics.BitmapFactory
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.convertDateToString
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.convertDateToStringDDMMM
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.formatToServerDateTimeDefaults
import br.com.angelorobson.alternativescene.application.commom.utils.extensions.formatToViewDateTimeDefaults
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

@BindingAdapter( "imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .into(view)
}

@BindingAdapter( "loadImageFromPath")
fun loadImageFromPath(view: ImageView, path: String) {
    view.setImageBitmap(BitmapFactory.decodeFile(path))
}

@BindingAdapter( "status")
fun status(imageView: ImageView, status: Boolean) {
    if (status) {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_check))
    } else {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_error))
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