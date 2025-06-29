package com.example.medicapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.medicapp.databinding.ActivityMedicationDetailBinding
import com.example.medicapp.model.Medication

class MedicationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMedicationDetailBinding
    private var medication: Medication? = null

    companion object {
        private const val EXTRA_MEDICATION = "extra_medication"

        fun createIntent(context: Context, medication: Medication): Intent {
            return Intent(context, MedicationDetailActivity::class.java).apply {
                putExtra(EXTRA_MEDICATION, medication)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        medication = intent.getParcelableExtra(EXTRA_MEDICATION)
        medication?.let { displayMedicationDetails(it) }
        setupPdfButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Medication Details"
        }
    }

    private fun displayMedicationDetails(medication: Medication) {
        binding.apply {
            medicationName.text = medication.name
            medicationDescription.text = medication.description
            medicationPrice.text = String.format("$%.2f", medication.price)

            // Load medication image
            Glide.with(this@MedicationDetailActivity)
                .load(medication.imageUrl)
                .centerCrop()
                .into(medicationImage)

            // Load supplier information
            supplierName.text = medication.supplier.name
            Glide.with(this@MedicationDetailActivity)
                .load(medication.supplier.imageUrl)
                .centerCrop()
                .into(supplierImage)
        }
    }

    private fun setupPdfButton() {
        binding.viewPdfButton.setOnClickListener {
            medication?.let { med ->
                startActivity(PdfViewerActivity.createIntent(this, med.pdfUrl))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
