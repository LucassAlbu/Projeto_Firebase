package com.projetoFirebase.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.albuquerque.projetoFirebase.databinding.SupportFragmentBinding
import com.projetoFirebase.presentation.MainViewModel

class SupportFragment : Fragment() {

    private var _binding: SupportFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SupportFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigate()
        setToolbar()
    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(true)
        viewModel.setCheckableBottomNavigationItem(true)
    }

    private fun setToolbar() {
        viewModel.toolbarVisibility(true)
        viewModel.setToolbarTitleText("Suporte")
        viewModel.toolbarBackButtonVisibility(false)
    }

}