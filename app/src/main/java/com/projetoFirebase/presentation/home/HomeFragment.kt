package com.projetoFirebase.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.albuquerque.projetoFirebase.databinding.HomeFragmentBinding
import com.projetoFirebase.presentation.MainViewModel

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomeFragmentBinding.inflate(inflater, container, false)
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
        viewModel.setToolbarTitleText("Home")
        viewModel.toolbarBackButtonVisibility(false)
    }

    private fun initClicks() {

    }

}