package com.example.medicapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.barteksc.pdfviewer.util.FitPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.net.URL
import com.example.medicapp.databinding.ActivityPdfViewerBinding

class PdfViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfViewerBinding

    companion object {
        private const val EXTRA_PDF_URL = "extra_pdf_url"

        fun createIntent(context: Context, pdfUrl: String): Intent {
            return Intent(context, PdfViewerActivity::class.java).apply {
                putExtra(EXTRA_PDF_URL, pdfUrl)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadPdf()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "PDF Documentation"
        }
    }

    private fun loadPdf() {
        val pdfUrl = intent.getStringExtra(EXTRA_PDF_URL)
        if (pdfUrl == null) {
            Toast.makeText(this, "Error: PDF URL not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.progressBar.show()
        
        lifecycleScope.launch {
            try {
                val inputStream = withContext(Dispatchers.IO) {
                    URL(pdfUrl).openStream()
                }
                
                binding.pdfView.fromStream(BufferedInputStream(inputStream))
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .spacing(10)
                    .load()
                
                binding.progressBar.hide()
            } catch (e: Exception) {
                binding.progressBar.hide()
                Toast.makeText(
                    this@PdfViewerActivity,
                    "Error loading PDF: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                finish()
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
