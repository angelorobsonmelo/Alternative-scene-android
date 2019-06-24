package br.com.angelorobson.alternativescene.application.modules.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.alternativescene.R
import br.com.angelorobson.alternativescene.databinding.EventItemBinding
import br.com.angelorobson.alternativescene.domain.Event
import br.com.soluevo.genericviewholderlibrary.GenericViewHolder

class EventsAdapter(private var mEvents: MutableList<Event>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GenericViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.event_item, parent, false)
        )
    }

    override fun getItemCount() = mEvents.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val biding = (holder as GenericViewHolder).binding as EventItemBinding
        biding.event = mEvents[position]
    }

    fun updateItems(events: List<Event>) {
        mEvents.addAll(events)
        notifyDataSetChanged()
    }


}