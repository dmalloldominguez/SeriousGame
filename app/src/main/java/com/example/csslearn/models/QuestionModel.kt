package com.example.csslearn.models

// El modelo de datos de la pregunta
data class QuestionModel(
    val id: String? = null,
    val question: String,
    val incorrectAnswers: List<String>,
    val correctAnswer: String
)