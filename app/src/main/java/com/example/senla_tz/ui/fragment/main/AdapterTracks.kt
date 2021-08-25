package com.example.senla_tz.ui.fragment.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.senla_tz.databinding.ItemTrackBinding
import com.example.senla_tz.entify.Track

class AdapterTracks(private val list: List<Track>, private inline val onClick:(Track) -> Unit):
    RecyclerView.Adapter<AdapterTracks.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(ItemTrackBinding.inflate(LayoutInflater.from(parent.context),parent, false))

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.binding.apply {
            val track = list[position]
            model = track

            root.setOnClickListener {
              onClick(track)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class TrackViewHolder(val binding: ItemTrackBinding): RecyclerView.ViewHolder(binding.root)
}