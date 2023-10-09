package com.projetoFirebase.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albuquerque.projetoFirebase.R
import com.albuquerque.projetoFirebase.databinding.SplashFragmentBinding

class SplashFragment : Fragment() {

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigate()
        showStatusBar(false)
    }

    override fun onStart() {
        super.onStart()

        val currentUser = viewModel.currentUserFB
        if (currentUser != null) {
            Handler(Looper.getMainLooper()).postDelayed(
                { findNavController().navigate(R.id.action_splashFragment_to_homeFragment) },
                3000
            )

        } else {
            Handler(Looper.getMainLooper()).postDelayed(
                { findNavController().navigate(R.id.action_splashFragment_to_loginFirebaseFragment) },
                3000
            )
        }
    }

    private fun showStatusBar(show: Boolean) {
        viewModel.showStatusBar(show)
    }

    private fun setBottomNavigate() {
        viewModel.bottomNavigateVisibility(false)
        viewModel.setCheckableBottomNavigationItem(false)
        viewModel.toolbarVisibility(false)
    }


}