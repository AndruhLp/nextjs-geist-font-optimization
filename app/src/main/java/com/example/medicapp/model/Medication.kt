package com.example.medicapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Medication(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val supplier: Supplier,
    val pdfUrl: String
) : Parcelable

@Parcelize
data class Supplier(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val products: List<String>
) : Parcelable
