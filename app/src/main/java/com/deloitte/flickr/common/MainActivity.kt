package com.deloitte.flickr.common

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.deloitte.flickr.R
import com.deloitte.flickr.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navHostFragment.navController.addOnDestinationChangedListener { _, dest, _ ->
            when (dest.id) {
                R.id.mainFragment -> enableToolbarBackButton(false)
                else -> enableToolbarBackButton(true)
            }
        }
    }

    private fun enableToolbarBackButton(enabled: Boolean) {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(enabled)
            it.setDisplayShowHomeEnabled(enabled)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}