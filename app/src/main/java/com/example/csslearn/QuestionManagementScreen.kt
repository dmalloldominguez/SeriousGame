package com.example.csslearn

import QuestionViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.csslearn.models.QuestionModel
import kotlinx.coroutines.launch

// Pantalla que mostrará un formulario para crear preguntas y un listado de preguntas
// con la posibilidad de editarlas y eliminarlas
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun QuestionManagementScreen(viewModel: QuestionViewModel, modifier: Modifier = Modifier) {
    val questions by viewModel.questions.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Cargar las preguntas tras iniciar la pantalla
    coroutineScope.launch { viewModel.loadQuestions() }

    Column(modifier = modifier.fillMaxSize()) {
        // Formulario para crear preguntas
        CreateQuestionForm { newQuestion ->
            coroutineScope.launch {
                viewModel.createQuestion(newQuestion)
            }
        }

        // Listado de preguntas
        if (questions.isEmpty()) {
            Text("No questions available.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Cada pregunta se mostrará en un Card con la posibilidad de editarla y eliminarla
                // Las funciones onUpdateQuestion y onDeleteQuestion se pasan como parámetros
                items(questions) { question ->
                    QuestionItem(
                        question = question,
                        onUpdateQuestion = { updatedQuestion ->
                            viewModel.updateQuestion(updatedQuestion)
                        },
                        onDeleteQuestion = {
                            coroutineScope.launch {
                                viewModel.deleteQuestion(question.id!!)
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}


// Card para ver cada una de las preguntas
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuestionItem(
    question: QuestionModel,
    onUpdateQuestion: (QuestionModel) -> Unit,
    onDeleteQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Vamos a guardar los valores de la pregunta editada y los valores editados
    var isEditing by remember { mutableStateOf(false) }
    var editedQuestion by remember { mutableStateOf(question.question) }
    var editedIncorrectAnswers by remember { mutableStateOf(question.incorrectAnswers) }
    var editedCorrectAnswer by remember { mutableStateOf(question.correctAnswer) }

    Card(modifier = modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cuando se está editando la pregunta, mostramos los campos de texto para editar la pregunta
            if (isEditing) {
                OutlinedTextField(
                    value = editedQuestion,
                    onValueChange = { editedQuestion = it },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )

                editedIncorrectAnswers.forEachIndexed { index, answer ->
                    OutlinedTextField(
                        value = answer,
                        // Cada vez que cambia un valor de las respuestas incorrectas, actualizamos la lista entera de respuestas incorrectas
                        onValueChange = { newValue ->
                            editedIncorrectAnswers = editedIncorrectAnswers.toMutableList().apply {
                                this[index] = newValue
                            }
                        },
                        label = { Text("Incorrect Answer ${index + 1}") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = editedCorrectAnswer,
                    onValueChange = { editedCorrectAnswer = it },
                    label = { Text("Correct Answer") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val updatedQuestion = question.copy(
                            question = editedQuestion,
                            incorrectAnswers = editedIncorrectAnswers,
                            correctAnswer = editedCorrectAnswer
                        )
                        // Actualizamos la pregunta
                        onUpdateQuestion(updatedQuestion)
                        isEditing = false
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Save")
                }
            // Si no se está editando, mostramos la pregunta y las respuestas
            } else {
                Text(text = question.question, style = MaterialTheme.typography.headlineSmall)
                val allOptions = question.incorrectAnswers + question.correctAnswer
                allOptions.forEach { option ->
                    Chip(
                        onClick = {},
                        colors = if (option == question.correctAnswer) {
                            ChipDefaults.chipColors(backgroundColor = Color.Green.copy(alpha = 0.2f))
                        } else {
                            ChipDefaults.chipColors()
                        }
                    ) {
                        Text(text = option)
                    }
                }

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onDeleteQuestion,
                        modifier = Modifier.weight(1f),

                        ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

// Formulario para crear preguntas
@Composable
fun CreateQuestionForm(onCreateQuestion: (QuestionModel) -> Unit) {
    var questionText by remember { mutableStateOf("") }
    var incorrectAnswer1 by remember { mutableStateOf("") }
    var incorrectAnswer2 by remember { mutableStateOf("") }
    var incorrectAnswer3 by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = questionText,
            onValueChange = { questionText = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            OutlinedTextField(
                value = incorrectAnswer1,
                onValueChange = { incorrectAnswer1 = it },
                label = { Text("Incorrect Answer 1") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            OutlinedTextField(
                value = incorrectAnswer2,
                onValueChange = { incorrectAnswer2 = it },
                label = { Text("Incorrect Answer 2") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
        Row {
            OutlinedTextField(
                value = incorrectAnswer3,
                onValueChange = { incorrectAnswer3 = it },
                label = { Text("Incorrect Answer 3") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            OutlinedTextField(
                value = correctAnswer,
                onValueChange = { correctAnswer = it },
                label = { Text("Correct Answer") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        Button(
            onClick = {
                val newQuestion = QuestionModel(
                    question = questionText,
                    incorrectAnswers = listOf(incorrectAnswer1, incorrectAnswer2, incorrectAnswer3),
                    correctAnswer = correctAnswer
                )
                onCreateQuestion(newQuestion)
                questionText = ""
                incorrectAnswer1 = ""
                incorrectAnswer2 = ""
                incorrectAnswer3 = ""
                correctAnswer = ""
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Create Question")
        }
    }
}