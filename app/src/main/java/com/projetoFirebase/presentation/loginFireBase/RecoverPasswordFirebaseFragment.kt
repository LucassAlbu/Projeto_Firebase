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
import com.albuquerque.projetoFirebase.databinding.RecoverPasswordFirebaseFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.projetoFirebase.presentation.MainViewModel

class RecoverPasswordFirebaseFragment : Fragment() {

    private var _binding: RecoverPasswordFirebaseFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = RecoverPasswordFirebaseFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        setBottomNavigate()
    }

    private fun initClicks() {

        binding.btnSend.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            viewModel.recoverAccountFireBase(email)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Recuperação de senha")
            builder.setCancelable(false)
            builder.setMessage("E-mail de recuperação enviado para sua caixa de mensagens")
            builder.setPositiveButton("Fechar") { dialog, _ ->
                dialog.dismiss()
                findNavController().navigate(R.id.action_recoverPasswordFirebaseFragment_to_loginFirebaseFragment)
            }
            builder.show()
        }
    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(false)
        viewModel.setCheckableBottomNavigationItem(false)
    }

}