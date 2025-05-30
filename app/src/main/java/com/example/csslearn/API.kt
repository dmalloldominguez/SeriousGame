package com.example.csslearn

import com.example.csslearn.services.QuestionApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Se debe modificar con la ubicación de la API
    // Si está en el mismo PC, poner la IP de la máquina
    private const val BASE_URL = "http://192.168.0.69:8080/"

    val instance: QuestionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionApiService::class.java)
    }

}