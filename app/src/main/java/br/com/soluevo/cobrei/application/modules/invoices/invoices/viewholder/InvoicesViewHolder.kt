package br.com.soluevo.cobrei.application.modules.invoices.invoices.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.soluevo.cobrei.databinding.InvoiceItemBinding

class InvoicesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding: InvoiceItemBinding? = DataBindingUtil.bind(view)
}