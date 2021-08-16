package com.example.senla_tz.ui.fragment.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.senla_tz.R
import com.example.senla_tz.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.senla_tz.ui.activity.main.IMainNavController
import com.example.senla_tz.util.resourse.RecyclerViewDecoration


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var mainNavController: IMainNavController? = null

    private val vm : MainFragmentViewModel by viewModels()
    private var binding: FragmentMainBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainNavController = context as IMainNavController
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = FragmentMainBinding.bind(view)

        binding?.apply {
            mainNavController?.addNavController(toolbar = toolbar)

            viewModel = vm
            recListTracks.addItemDecoration(RecyclerViewDecoration(ContextCompat.getDrawable(requireContext(), R.drawable.decoration_white)!!))
            recListTracks.adapter = AdapterTracks()
        }

        vm.getListTracks()

        initListener()
        initObserver()
    }

    private fun initListener() {

    }

    private fun initObserver() {

    }

    override fun onDetach() {
        mainNavController = null
        super.onDetach()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
