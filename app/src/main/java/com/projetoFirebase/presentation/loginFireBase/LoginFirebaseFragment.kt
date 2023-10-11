package com.projetoFirebase.presentation.loginFireBase

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albuquerque.projetoFirebase.R
import com.albuquerque.projetoFirebase.databinding.LoginFirebaseFragmentBinding
import com.projetoFirebase.presentation.MainViewModel
import com.projetoFirebase.presentation.UIState
import com.projetoFirebase.util.gone
import com.projetoFirebase.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFirebaseFragment : Fragment() {

    private var _binding: LoginFirebaseFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LoginFirebaseFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        initObservables()
        setBottomNavigate()
        showStatusBar(false)
    }

    private fun validation() {

        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            viewModel.uiStateLoginFB.value = UIState.EmptyState
        } else {
            viewModel.uiStateLoginFB.value = UIState.Loading
            viewModel.loginFireBase(email, password)
        }
    }

    private fun initClicks() {

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFirebaseFragment_to_recoverPasswordFirebaseFragment)
        }

        binding.btnEnter.setOnClickListener {
            validation()

        }

        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFirebaseFragment_to_registerFirebaseFragment)
        }
    }

    private fun uiStateManager(uiState: UIState) {
        when (uiState) {
            is UIState.AuthSuccess -> dismissLoading()
            is UIState.Loading -> setFbLoginLoading()
            is UIState.AuthError -> setFbLoginError()
            is UIState.EmptyState -> setFbEmpty()
            else -> return

        }
    }

    private fun setFbLoginError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Falha ao logar")
        builder.setCancelable(false)
        builder.setMessage("O E-mail ou senha inseridos estão incorretos, tente novamente!")
        builder.setPositiveButton("Fechar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
        binding.loading.gone()
    }

    private fun setFbEmpty() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Falha ao logar")
        builder.setCancelable(false)
        builder.setMessage("O campo de E-mail ou senha não foi preenchido!")
        builder.setPositiveButton("Fechar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }


    private fun setFbLoginLoading() {
        binding.loading.visible()
    }

    private fun dismissLoading() {
        binding.loading.gone()
        view?.post {
            findNavController().navigate(R.id.action_loginFirebaseFragment_to_homeFragment)
        }
    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(false)
        viewModel.setCheckableBottomNavigationItem(false)
        viewModel.toolbarVisibility(false)

    }

    private fun showStatusBar(show: Boolean) {
        viewModel.showStatusBar(show)
    }

    private fun initObservables() {
        viewModel.uiStateLoginFB.observe(requireActivity()) { uiStateLoginFirebase ->
            uiStateManager(uiStateLoginFirebase)

        }
    }
}