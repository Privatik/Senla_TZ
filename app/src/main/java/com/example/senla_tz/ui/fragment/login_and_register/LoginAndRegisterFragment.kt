package com.example.senla_tz.ui.fragment.login_and_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.senla_tz.R
import com.example.senla_tz.databinding.FragmentLoginAndRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginAndRegisterFragment : Fragment(R.layout.fragment_login_and_register) {

    private val viewModel: LoginAndRegisterViewModel by viewModels()
    private var binding: FragmentLoginAndRegisterBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginAndRegisterBinding.bind(view).also {
            it.viewModel = viewModel
        }


        initListener()
        intiOberver()
    }

    private fun initListener() {

    }

    private fun intiOberver() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}