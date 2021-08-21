package com.example.senla_tz.ui.fragment.login_and_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.senla_tz.R
import com.example.senla_tz.databinding.FragmentLoginAndRegisterBinding
import com.example.senla_tz.ui.activity.main.MainActivity
import com.example.senla_tz.ui.dialog.LoadDialog
import com.example.senla_tz.util.extends.closeLoadDialog
import com.example.senla_tz.util.extends.openLoadDialog
import com.example.senla_tz.util.extends.showSnackBar
import com.example.senla_tz.util.extends.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private val TAG : String = LoginAndRegisterFragment::class.java.simpleName
@AndroidEntryPoint
class LoginAndRegisterFragment : Fragment(R.layout.fragment_login_and_register) {

    private val vm: LoginAndRegisterViewModel by viewModels()
    private var binding: FragmentLoginAndRegisterBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginAndRegisterBinding.bind(view).also {
            it.viewModel = vm
        }


        initListener()
        intiObserver()
    }

    private fun initListener() {
        binding?.apply {
            btnLogin.setOnClickListener {
                if (vm.isLoginState.get()){
                    vm.login(
                        email = edEmail.text.toString(),
                        password = edPassword.text.toString()
                    )
                    openLoadDialog()
                } else {
                    if (edPassword.text.toString() == edConfirmPassword.text.toString()) {
                        vm.register(
                            email = edEmail.text.toString(),
                            password = edPassword.text.toString(),
                            fistName = edFistName.text.toString(),
                            lastName = edLastName.text.toString()
                        )
                        openLoadDialog()
                    }else {
                        showSnackBar("Пароли не совпадают")
                    }
                }
            }

            btnChangeStateLogin.setOnClickListener {
                Log.e(TAG,"click change")
                vm.isLoginState.apply {
                    set(!get())
                }
            }
        }
    }

    private fun intiObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.authorizationFlow.collect {
                    closeLoadDialog()

                    showToast(it,short = true)
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                vm.authorizationFailFlow.collect {
                    closeLoadDialog()

                    showSnackBar(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}