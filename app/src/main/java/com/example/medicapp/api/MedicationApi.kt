package com.example.medicapp.api

import com.example.medicapp.model.Medication
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MedicationApi {
    @GET("medications")
    suspend fun getMedications(): Response<List<Medication>>

    @GET("medications/{id}")
    suspend fun getMedicationById(@Path("id") id: Int): Response<Medication>
}
