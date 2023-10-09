package com.projetoFirebase.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.albuquerque.projetoFirebase.databinding.CameraFragmentBinding
import com.projetoFirebase.presentation.MainViewModel

class CameraFragment : Fragment() {

    private var _binding: CameraFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = CameraFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        setBottomNavigate()
        showStatusBar(true)
        setToolbar()
    }

    private fun showStatusBar(show: Boolean) {
        viewModel.showStatusBar(show)
    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(true)
        viewModel.setCheckableBottomNavigationItem(true)
    }

    private fun setToolbar() {
        viewModel.toolbarVisibility(true)
        viewModel.setToolbarTitleText("Cameras")
        viewModel.toolbarBackButtonVisibility(false)
    }

    private fun initClicks() {

    }

}