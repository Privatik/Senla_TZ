package com.example.senla_tz.ui.fragment.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.senla_tz.databinding.ItemTrackBinding
import com.example.senla_tz.entify.Track

class AdapterTracks(private inline val onClick:(Track) -> Unit):
    ListAdapter<Track, AdapterTracks.TrackViewHolder>(MyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(ItemTrackBinding.inflate(LayoutInflater.from(parent.context),parent, false))

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.binding.apply {
            val track = getItem(position)
            model = track

            root.setOnClickListener {
              onClick(track)
            }
        }
    }

    class TrackViewHolder(val binding: ItemTrackBinding): RecyclerView.ViewHolder(binding.root)
}

class MyDiffUtil: DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(
        oldConcert: Track,
        newConcert: Track
    ) = oldConcert.id == newConcert.id

    override fun areContentsTheSame(
        oldConcert: Track,
        newConcert: Track
    ) = oldConcert == newConcert
}
