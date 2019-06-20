package br.com.angelorobson.alternativescene.application.commom.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class GenericViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding: ViewDataBinding? = DataBindingUtil.bind(view)

}