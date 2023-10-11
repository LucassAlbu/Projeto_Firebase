package com.projetoFirebase.presentation.profileFirebase

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albuquerque.projetoFirebase.R
import com.albuquerque.projetoFirebase.databinding.ProfileFirebaseFragmentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.presentation.MainViewModel
import com.projetoFirebase.presentation.UIState
import com.projetoFirebase.util.gone
import com.projetoFirebase.util.visible


class ProfileFirebaseFragment : Fragment() {

    private var _binding: ProfileFirebaseFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ProfileFirebaseFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfileData()
        initObservables()
        initClicks()
        setBottomNavigate()
        setToolbar()
    }

    private fun getProfileData() {
        viewModel.getUserDataFireStore()
    }

    private fun uiStateManager(uiState: UIState) {
        when (uiState) {
            is UIState.FireStoreSuccess -> setUserData(uiState.userDTO)
            is UIState.FireStoreError -> buildErrorDialog()
            is UIState.Loading -> setLoading()
            is UIState.FireBaseLogout -> return
            else -> return
        }
    }


    private fun setLoading() {
        binding.loading.visible()
    }

    private fun dismissLoading() {
        binding.loading.gone()
    }

    private fun setUserData(userDTO: UserDTO) {

        binding.edtUserName.text = userDTO.name.toString()
        binding.edtCpf.text = userDTO.cpf.toString()
        binding.edtEmail.text = userDTO.email.toString()
        binding.edtUserPhone.text = userDTO.phone.toString()
        binding.edtBirthDate.text = userDTO.birthDate.toString()

        dismissLoading()
    }

    private fun buildErrorDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Deu erro ai man")
        builder.setCancelable(false)
        builder.setMessage("error")
        builder.setPositiveButton("Fechar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(true)
        viewModel.setCheckableBottomNavigationItem(true)
    }

    private fun setToolbar() {
        viewModel.toolbarVisibility(true)
        viewModel.setToolbarTitleText("Perfil")
        viewModel.toolbarBackButtonVisibility(false)
    }

    private fun initClicks() {
        binding.btnChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_profileFirebaseFragment_to_changePasswordFirebaseFragment)
        }
        binding.btnUpdateProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFirebaseFragment_to_updateProfileFragment)
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_menu_profile_to_loginFirebaseFragment)
            uiStateManager(UIState.FireBaseLogout)
        }
    }


    private fun initObservables() {
        viewModel.uiStateGetUserDataFS.observe(requireActivity()) { uiStateGetUserDataFS ->
            uiStateManager(uiStateGetUserDataFS)
        }
    }
}