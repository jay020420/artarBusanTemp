package com.artar.busan.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artar.busan.R
import com.artar.busan.checkin.StampStore
import com.artar.busan.model.Event

class EventSelectionFragment : Fragment() {

    private val viewModel: EventSelectionViewModel by activityViewModels {
        AppViewModelFactory(StampStore(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_event_selection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.eventRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val adapter = EventAdapter { event ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ArExperienceFragment.newInstance(event.id, event.name, event.primaryColor))
                .addToBackStack(null)
                .commit()
        }
        recycler.adapter = adapter

        viewModel.events.observe(viewLifecycleOwner) { adapter.submit(it) }
        viewModel.loadEvents()
    }

    companion object {
        fun newInstance() = EventSelectionFragment()
    }

    private class EventAdapter(
        private val onClick: (Event) -> Unit
    ) : RecyclerView.Adapter<EventAdapter.VH>() {

        private var items: List<Event> = emptyList()

        fun submit(data: List<Event>) {
            items = data
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event_card, parent, false)
            return VH(view, onClick)
        }

        override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

        override fun getItemCount(): Int = items.size

        class VH(itemView: View, private val onClick: (Event) -> Unit) : RecyclerView.ViewHolder(itemView) {
            fun bind(event: Event) {
                val title = itemView.findViewById<TextView>(R.id.eventName)
                title.text = event.name
                runCatching { itemView.setBackgroundColor(Color.parseColor(event.primaryColor)) }
                itemView.setOnClickListener { onClick(event) }
            }
        }
    }
}
