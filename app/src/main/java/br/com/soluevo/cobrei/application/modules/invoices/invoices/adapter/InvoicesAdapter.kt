package br.com.soluevo.cobrei.application.modules.invoices.invoices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.soluevo.cobrei.R
import br.com.soluevo.cobrei.domain.response.InvoiceResponse
import br.com.soluevo.cobrei.application.modules.invoices.invoices.viewholder.InvoicesViewHolder

class InvoicesAdapter(
    private val invoices: MutableList<InvoiceResponse>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InvoicesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.invoice_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as InvoicesViewHolder).binding
        binding?.invoice = invoices[position]
        binding?.executePendingBindings()
    }

    override fun getItemCount() = invoices.size
    fun updateItems(proposals: MutableList<InvoiceResponse>) {
        this.invoices.clear()
        this.invoices.addAll(proposals)
        notifyDataSetChanged()
    }

}