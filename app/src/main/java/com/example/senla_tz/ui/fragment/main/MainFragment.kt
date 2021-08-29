package com.example.senla_tz.ui.fragment.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.senla_tz.R
import com.example.senla_tz.bind_adapter.isLoginState
import com.example.senla_tz.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.senla_tz.ui.activity.main.IMainNavController
import com.example.senla_tz.ui.activity.run.RunActivity
import com.example.senla_tz.ui.activity.run.RunUtility
import com.example.senla_tz.util.Constant
import com.example.senla_tz.util.extends.closeLoadDialog
import com.example.senla_tz.util.extends.isLoadState
import com.example.senla_tz.util.extends.openLoadDialog
import com.example.senla_tz.util.extends.showSnackBar
import com.example.senla_tz.util.resourse.RecyclerViewDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private val TAG = MainFragment::class.java.simpleName
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var mainNavController: IMainNavController? = null

    private val vm : MainFragmentViewModel by viewModels()
    private var binding: FragmentMainBinding? = null

    private var adapterTracks: AdapterTracks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainNavController = context as IMainNavController
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        openLoadDialog()

        binding = FragmentMainBinding.bind(view)

        binding?.apply {
            mainNavController?.addNavController(toolbar = toolbar)

            viewModel = vm
            adapterTracks = AdapterTracks{ track ->
            startActivity(Intent(requireActivity(), RunActivity::class.java)
                .putExtra(Constant.VIEW_OLD_TRACK,true)
                .putExtra(Constant.TRACK,track))
            }

            recListTracks.adapter = adapterTracks

            refreshListTracks.apply {
                setColorSchemeResources(R.color.white)
                setProgressBackgroundColorSchemeResource(R.color.purple_700)

                setOnRefreshListener {
                    vm.getListTracks()
                }
            }
            //recListTracks.addItemDecoration(RecyclerViewDecoration(ContextCompat.getDrawable(requireContext(), R.drawable.decoration_white)!!))
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
       binding?.apply {
           fabRunScreen.setOnClickListener {
               startActivity(Intent(requireContext().applicationContext, RunActivity::class.java))
           }
       }
    }

    private fun initObserver() {
       lifecycleScope.launch {
           lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
               vm.tracksFlow.collect {
                   if(isLoadState()) closeLoadDialog()
                   binding?.refreshListTracks?.apply { if (isRefreshing) post {isRefreshing = false} }
                   adapterTracks?.submitList(it.reversed())
               }
           }
       }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.tracksFailFlow.collect {
                    if(isLoadState()) closeLoadDialog()
                    showSnackBar(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.getListTracks()
        Log.e(TAG,"Resume")
    }


    override fun onDetach() {
        mainNavController = null
        super.onDetach()
    }

    override fun onDestroyView() {
        binding = null
        adapterTracks = null
        super.onDestroyView()
    }
}
