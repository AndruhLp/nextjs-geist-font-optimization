package com.example.medicapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicapp.databinding.ActivityMainBinding
import com.example.medicapp.repository.MedicationRepository
import com.example.medicapp.ui.adapter.MedicationAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val repository = MedicationRepository()
    private lateinit var medicationAdapter: MedicationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        setupSwipeRefresh()
        loadMedications()
    }

    private fun setupRecyclerView() {
        medicationAdapter = MedicationAdapter { medication ->
            // Handle medication item click - Navigate to detail screen
            startActivity(MedicationDetailActivity.createIntent(this, medication))
        }
        
        binding.medicationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = medicationAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadMedications()
        }
    }

    private fun loadMedications() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            
            when (val result = repository.getMedications()) {
                is MedicationRepository.Result.Success -> {
                    medicationAdapter.submitList(result.data)
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is MedicationRepository.Result.Error -> {
                    Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
}
