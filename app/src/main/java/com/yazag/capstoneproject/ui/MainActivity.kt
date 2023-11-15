package com.yazag.capstoneproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.yazag.capstoneproject.R
import com.yazag.capstoneproject.common.gone
import com.yazag.capstoneproject.common.viewBinding
import com.yazag.capstoneproject.common.visible
import com.yazag.capstoneproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomMenu, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.splashFragment,
                R.id.signUpFragment,
                R.id.signInFragment,
                R.id.detailFragment,
                R.id.paymentFragment,
                R.id.paymentSuccessFragment
                -> {
                    binding.bottomMenu.gone()
                }

                else -> {
                    binding.bottomMenu.visible()
                }
            }
        }
    }
}