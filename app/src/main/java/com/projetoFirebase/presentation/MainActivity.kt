package com.projetoFirebase.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.albuquerque.projetoFirebase.R
import com.albuquerque.projetoFirebase.databinding.ActivityMainBinding
import com.projetoFirebase.util.gone
import com.projetoFirebase.util.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initNavigation()
        initObervables()

    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(
            binding.btnv,
            navController
        )

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setBottomNavigateVisibility(bottomNavigateIsVisible: Boolean) {

        if (bottomNavigateIsVisible) {
            binding.btnv.visible()
        } else {
            binding.btnv.gone()
        }
    }

    private fun setCheckableBottomNavigationItem(check: Boolean) {
        binding.btnv.menu.setGroupCheckable(0, check, true)
    }

    private fun setToolbarVisibility(toolbarVisibility: Boolean) {
        if (toolbarVisibility) {
            binding.toolbar.visible()
        } else {
            binding.toolbar.gone()
        }
    }

    private fun setToolbarBackButtonVisibility(toolbarBackButtonVisibility: Boolean) {
        binding.toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_arrow)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        if (toolbarBackButtonVisibility) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun changeStatusBarColorForSplashScreen() {

        window.statusBarColor = getColor(R.color.white)
    }

    private fun changeStatusBarColorToDefault() {

        window.statusBarColor = getColor(R.color.primaria02)
    }

    private fun initObervables() {

        viewModel.bottomNavigateIsVisible.observe(this) {
            setBottomNavigateVisibility(it)
        }

//        viewModel.checkBottomNavigationItem.observe(this) {
//            setCheckableBottomNavigationItem(it)
//        }
        viewModel.toolbarIsvisible.observe(this) {
            setToolbarVisibility(it)
        }
        viewModel.toolbarTitleText.observe(this) {
            binding.toolbarText.text = it

        }
        viewModel.toolbarBackButtonIsVisible.observe(this) {
            setToolbarBackButtonVisibility(it)
        }

        viewModel.statusBarIsVisible.observe(this) {
            if (it) {
                changeStatusBarColorToDefault()
            } else {
                changeStatusBarColorForSplashScreen()
            }
        }

    }
}