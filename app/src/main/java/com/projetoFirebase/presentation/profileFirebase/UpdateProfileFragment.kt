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
import com.albuquerque.projetoFirebase.databinding.UpdateProfileFragmentBinding
import com.google.firebase.auth.EmailAuthProvider
import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.presentation.MainViewModel
import com.projetoFirebase.presentation.UIState
import com.projetoFirebase.util.gone
import com.projetoFirebase.util.visible
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class UpdateProfileFragment : Fragment() {

    private var _binding: UpdateProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = UpdateProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservables()
        setBottomNavigate()
        setToolbar()
        initClicks()
    }

    private fun dateIsValid(data: String): Boolean {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        try {
            val convertedDate = LocalDate.parse(data, formato)
            val today = LocalDate.now()

            val diference = Period.between(convertedDate, today)

            return diference.years in 1..99
        } catch (e: DateTimeParseException) {

            return false
        }
    }

    private fun setUserData(userDTO: UserDTO) {

        binding.edtUserName.setText(userDTO.name.toString())
        binding.edtEmail.setText(userDTO.email.toString())
        binding.edtUserPhone.setText(userDTO.phone.toString())
        binding.edtBirthDate.setText(userDTO.birthDate.toString())

    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim()
        val name = binding.edtUserName.text.toString().trim()
        val currentEmail = viewModel.currentUserFB?.email
        val birthDate = binding.edtBirthDate.text.toString().trim()
        val phone = binding.edtUserPhone.text.toString().trim()

        if (email.isNotEmpty() && name.isNotEmpty() && birthDate.isNotEmpty() && phone.isNotEmpty()) {
            if (dateIsValid(birthDate)) {
                binding.tvInvalidDate.gone()
                if (email == currentEmail) {
                    updateUserDAta(email, name, birthDate, phone)
                } else {
                    changeEmail(email, currentEmail!!)
                    updateUserDAta(email, name, birthDate, phone)
                }
            } else {
                binding.tvInvalidDate.visible()
            }
        } else {
            builderEmptyDialog()
        }
    }

    private fun updateUserDAta(email: String, name: String, birthDate: String, phone: String) {
        val userDTO = UserDTO(
            email = email,
            name = name,
            birthDate = birthDate,
            phone = phone,
            password = viewModel.userDTO.value?.password
        )
        viewModel.updateUserDataFireStore(userDTO)
        findNavController().navigate(R.id.action_updateProfileFragment_to_profileFirebaseFragment)
    }

    private fun initClicks() {
        binding.btnUpdateProfile.setOnClickListener {
            validateData()

        }
    }

    private fun changeEmail(email: String, currentEmail: String) {
        val password = viewModel.userDTO.value?.password.toString()

        val user = viewModel.currentUserFB

        val credential = EmailAuthProvider.getCredential(currentEmail, password)
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("REautenticado", "Usuário reautenticado.")
                    user.updateEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("alteração do E-mail", "User email address updated.")
                            } else {
                                Log.d("erro", "changeEmail: deu ruim")
                            }
                        }
                }
            }
    }

    private fun builderEmptyDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Falha ao atualizar dados")
        builder.setCancelable(false)
        builder.setMessage("Algum campo foi deixado em branco, favor preencher todos os dados")
        builder.setPositiveButton("Fechar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
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

    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(false)
        viewModel.setCheckableBottomNavigationItem(false)
    }

    private fun setToolbar() {
        viewModel.toolbarVisibility(true)
        viewModel.setToolbarTitleText("Atualizar cadastro")
        viewModel.toolbarBackButtonVisibility(true)
    }

    private fun initObservables() {
        viewModel.uiStateSignUpFB.observe(requireActivity()) { uiStateSignUpFirebase ->
            uiStateManager(uiStateSignUpFirebase)
        }
        viewModel.userDTO.observe(requireActivity()) { userAla ->
            setUserData(userAla)
        }
    }
}