package com.example.senla_tz.ui.fragment.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.senla_tz.databinding.ItemTrackBinding

class AdapterTracks: RecyclerView.Adapter<AdapterTracks.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(ItemTrackBinding.inflate(LayoutInflater.from(parent.context),parent, false))

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 40

    class TrackViewHolder(val binding: ItemTrackBinding): RecyclerView.ViewHolder(binding.root)
}