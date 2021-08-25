package com.example.senla_tz.ui.fragment.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senla_tz.entify.Track
import com.example.senla_tz.repository.TracksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val repository: TracksRepository
) : ViewModel() {

    val tracksFlow : SharedFlow<List<Track>> by lazy {
        repository.tracksFlow
    }

    val tracksFailFlow : SharedFlow<String> by lazy {
        repository.tracksFailFlow
    }

    fun getListTracks(){
        viewModelScope.launch {
            repository.getTracks()
        }
    }
}