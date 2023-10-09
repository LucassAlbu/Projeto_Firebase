package com.projetoFirebase.presentation.loginFireBase

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albuquerque.projetoFirebase.R
import com.albuquerque.projetoFirebase.databinding.RegisterFirebaseFragmentBinding
import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.presentation.MainViewModel
import com.projetoFirebase.presentation.UIState
import com.projetoFirebase.util.gone
import com.projetoFirebase.util.isCPF
import com.projetoFirebase.util.visible

class RegisterFirebaseFragment : Fragment() {

    private var _binding: RegisterFirebaseFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = RegisterFirebaseFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()
        initObservables()
        setBottomNavigate()
        termsAndConditions()
    }

    private fun registerUser() {

        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val cpf = binding.edtCpf.text.toString()
        val name = binding.edtUserName.text.toString().trim()
        val checked = binding.checkbox.isChecked


        if (validateData(email, cpf, password, name)) {
            if (checked) {

                viewModel.uiStateSignUpFB.value = UIState.Loading

                val userDTO = UserDTO(name = name, cpf = cpf, email = email, password = password)

                viewModel.signUpFirebase(userDTO)

            } else {
                dialogUseTerms()
            }
        }
    }

    private fun validateData(
        email: String,
        cpf: String,
        password: String,
        name: String
    ): Boolean {

        val cpfValid = isCPF(cpf)
        val emailConfirmation = binding.edtConfirmEmail.text.toString()
        val emailValid = isEmailValid(email)
        var emailIsEqual = false

        if (cpfValid) {
            binding.tvInvalidCpf.gone()

        } else {
            binding.tvInvalidCpf.visible()
        }

        if (emailValid) {
            binding.tvInvalidEmail.gone()

        } else {
            binding.tvInvalidEmail.visible()
        }

        if (emailConfirmation.isEmpty()) {
            binding.tvEmailNotMatch.visible()

        } else {
            if (email == emailConfirmation) {

                binding.tvEmailNotMatch.gone()
                emailIsEqual = true

            } else {

                binding.tvEmailNotMatch.visible()
            }
        }

        if (name.isEmpty()) {

            binding.tvInvalidName.visible()
        } else {

            binding.tvInvalidName.gone()
        }

        return isCPF(cpf) && isEmailValid(email) && emailIsEqual && name.isNotEmpty() && password.isNotEmpty()
    }

    private fun dialogUseTerms() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.register_firebase_error_register_account_dialog_title))
        builder.setCancelable(false)
        builder.setMessage(resources.getString(R.string.register_firebase_terms_and_conditions_dialog_text))
        builder.setPositiveButton("Fechar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
        binding.loading.gone()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun termsAndConditions() {
        val textView = binding.tvTermsAndConditions
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initClicks() {
        binding.btnCreateAccount.setOnClickListener {
            registerUser()
        }
    }

    private fun uiStateManager(uiState: UIState) {
        when (uiState) {
            is UIState.AuthSuccess -> dismissLoading()
            is UIState.Loading -> setFbLoginLoading()
            is UIState.AuthError -> dialogFireStoreRegisterError(uiState.error, "Erro ao cadastrar")
            else -> return
        }
    }

    private fun dialogFireStoreRegisterError(error: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setMessage(error)
        builder.setPositiveButton("Fechar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
        binding.loading.gone()
    }

    private fun setFbLoginLoading() {
        binding.loading.visible()
    }

    private fun dismissLoading() {
        binding.loading.gone()
        findNavController().navigate(R.id.action_registerFirebaseFragment_to_homeFragment)
    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(false)
        viewModel.setCheckableBottomNavigationItem(false)
    }

    private fun initObservables() {
        viewModel.uiStateSignUpFB.observe(requireActivity()) { uiStateSignUpFirebase ->
            uiStateManager(uiStateSignUpFirebase)
        }
    }

}


