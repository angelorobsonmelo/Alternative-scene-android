package br.com.angelorobson.alternativescene.application.modules.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.application.commom.viewholder.GenericViewHolder
import br.com.angelorobson.alternativescene.databinding.EventItemBinding
import br.com.angelorobson.alternativescene.domain.Event

class EventsAdapter(private val events: List<Event>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GenericViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.event_item, parent, false)
        )
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val biding = (holder as GenericViewHolder).binding as EventItemBinding
    }


}