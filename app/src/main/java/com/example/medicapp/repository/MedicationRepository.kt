package com.example.medicapp.repository

import com.example.medicapp.api.ApiClient
import com.example.medicapp.model.Medication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicationRepository {
    private val api = ApiClient.medicationApi

    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

    suspend fun getMedications(): Result<List<Medication>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMedications()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error fetching medications: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }

    suspend fun getMedicationById(id: Int): Result<Medication> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMedicationById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error fetching medication: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }
}
