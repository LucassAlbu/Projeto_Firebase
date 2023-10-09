package com.projetoFirebase.presentation.profileFirebase

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albuquerque.projetoFirebase.R
import com.albuquerque.projetoFirebase.databinding.ChangePasswordFirebaseFragmentBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.presentation.MainViewModel
import com.projetoFirebase.presentation.UIState
import com.projetoFirebase.util.gone
import com.projetoFirebase.util.visible


class ChangePasswordFirebaseFragment : Fragment() {

    private var _binding: ChangePasswordFirebaseFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth

        _binding = ChangePasswordFirebaseFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  initObservables()
        setToolbar()
        setBottomNavigate()
        initClicks()
    }

    private fun initClicks() {
        binding.btnChangePassword.setOnClickListener {
            validatePassword()
        }
    }


    private fun validatePassword() {
        val currentPassword = binding.edtCurrentPassword.text?.toString()?.trim()
        val newPassWord = binding.edtNewPassword.text?.toString()?.trim()
        val validatePassword = binding.edtConfirmPassword.text.toString().trim()
        val fbpassword = viewModel.userDTO.value?.password.toString().trim()

        if (newPassWord != validatePassword) {
            binding.tvPasswordNotMatch.visible()
        } else {
            binding.tvPasswordNotMatch.gone()
            if (fbpassword != currentPassword) {
                binding.tvIncorrectCurrentPassword.visible()
            } else {
                binding.tvIncorrectCurrentPassword.gone()
                changePassword(newPassWord, currentPassword)
                updatePassword(newPassWord)
                buildSuccessDialog()
            }
        }
    }

    private fun changePassword(newPassword: String, currentPassword: String) {
        val user = viewModel.currentUserFB
        val email = viewModel.currentUserFB?.email.toString()
        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("REautenticado", "Usuário reautenticado.")

                    user!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Nova senha", "User password updated.")
                            }
                        }
                }
            }
    }

    private fun updatePassword(newPassword: String) {
        val userDTO = UserDTO(
            password = newPassword,
            name = viewModel.userDTO.value?.name,
            birthDate = viewModel.userDTO.value?.birthDate,
            email = viewModel.userDTO.value?.email,
            phone = viewModel.userDTO.value?.phone
        )
        viewModel.updateUserDataFireStore(userDTO)
    }

    private fun uiStateManager(uiState: UIState) {
        when (uiState) {
            is UIState.AuthSuccess -> dismissLoading()
            is UIState.Loading -> setLoading()
            is UIState.AuthError -> buildSuccessDialog()
            else -> return
        }
    }

    private fun buildSuccessDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Senha alterada com sucesso")
        builder.setCancelable(false)
        builder.setMessage("Sua senha foi alterada com sucesso")
        builder.setPositiveButton("Voltar") { dialog, _ ->
            dialog.dismiss()
            findNavController().navigate(R.id.action_changePasswordFirebaseFragment_to_menu_profile)
        }
        builder.show()
    }

    private fun setLoading() {
        binding.loading.visible()
    }

    private fun dismissLoading() {
        binding.loading.gone()

    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(false)
        viewModel.setCheckableBottomNavigationItem(false)
    }

    private fun setToolbar() {
        viewModel.toolbarVisibility(true)
        viewModel.setToolbarTitleText("Alteração de senha")
        viewModel.toolbarBackButtonVisibility(true)
    }

    private fun initObservables() {
        viewModel.uiStateSignUpFB.observe(requireActivity()) { uiStateSignUpFirebase ->
            uiStateManager(uiStateSignUpFirebase)
        }
    }
}