package com.example.senla_tz.ui.fragment.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senla_tz.repository.MainFragmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val repository: MainFragmentRepository
) : ViewModel() {

    fun getListTracks(){
        viewModelScope.launch {
            repository.getTracks()
        }
    }
}