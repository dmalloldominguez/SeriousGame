package com.example.csslearn.services

import com.example.csslearn.models.QuestionModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// API para acceder a las preguntas usando Retrofit (vienen de una API REST hecha con Spring)
interface QuestionApiService {

    @GET("questions")
    suspend fun getAllQuestions(): List<QuestionModel>

    @GET("questions/{id}")
    suspend fun getQuestionById(@Path("id") id: String): QuestionModel

    @POST("questions")
    suspend fun createQuestion(@Body question: QuestionModel): QuestionModel

    @PUT("questions/{id}")
    suspend fun updateQuestion(@Path("id") id: String, @Body questionModel: QuestionModel): QuestionModel

    @DELETE("questions/{id}")
    suspend fun deleteQuestion(@Path("id") id: String)

    @GET("questions/default")
    suspend fun populateDatabase()
}