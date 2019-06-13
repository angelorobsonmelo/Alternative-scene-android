package br.com.angelorobson.alternativescene.application.modules.invoices.invoices.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.databinding.InvoiceItemBinding

class InvoicesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding: InvoiceItemBinding? = DataBindingUtil.bind(view)
}