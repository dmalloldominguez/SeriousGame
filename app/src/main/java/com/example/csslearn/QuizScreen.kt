package com.example.csslearn

import QuestionViewModel
import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.csslearn.models.QuestionModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Listado de imágenes que se usará aleatoriamente como fondo de cada pregunta
val backGroundImages = listOf(
    "https://img.freepik.com/free-vector/css-html-programming-languages-computer-programming-coding-it-female-programmer-cartoon-character-software-website-development-vector-isolated-concept-metaphor-illustration_335657-2740.jpg?t=st=1738620149~exp=1738623749~hmac=04238e98a42c44b17587e7588cc18d2be789750e8d20cf0eb2398f36dfbd67a0&w=400",
    "https://img.freepik.com/free-vector/front-end-concept-illustration_114360-22213.jpg?t=st=1738620665~exp=1738624265~hmac=b59784040121d56eff0e2161399990f7530ebaa25af5576915c4fd3237ce0f00&w=400",
    "https://img.freepik.com/free-vector/mobile-application-development-programming-languages-css-html-it-ui-male-programmer-cartoon-character-developing-website-coding_335657-2367.jpg?t=st=1738620680~exp=1738624280~hmac=c034346087d791e9a77a28f6935c511e7e7883c9c3780adc442d937de1092c13&w=400",
    "https://img.freepik.com/free-vector/programmer-working-with-css_52683-24172.jpg?t=st=1738620692~exp=1738624292~hmac=40c22b0ceac54e17d9d7f036eca9a6fd95f871377c9ce3143e5f6082b163976b&w=400",
    "https://img.freepik.com/free-vector/flat-cms-concept-illustrated_23-2148925794.jpg?t=st=1738620801~exp=1738624401~hmac=dd31ee2411f79b7cedc1db7b098fea6168dfe696ac5be23fc805e32da0da1bf1&w=400"
)

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun QuizScreen(viewModel: QuestionViewModel) {
    // Todas las preguntas
    val questions by viewModel.questions.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    // Mapa con las respuestas seleccionadas por el usuario
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<String, String>()) }
    var shuffledQuestions by remember { mutableStateOf(emptyList<QuestionModel>()) }

    // Cargamos 10 preguntas aleatorias al iniciar la pantalla
    coroutineScope.launch { viewModel.loadQuestions() }
        LaunchedEffect(questions) {
            shuffledQuestions = questions.shuffled().take(10)
    }

    // Contamos cuántas preguntas han sido respondidas
    val answeredQuestions = selectedAnswers.filter { it.value.isNotEmpty() }.size

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (shuffledQuestions.isEmpty()) {
            Text("No questions available.", modifier = Modifier.padding(16.dp))
        } else {
            AnswerStatus(answeredQuestions = answeredQuestions,  shuffledQuestions = shuffledQuestions, selectedAnswers = selectedAnswers)
            LazyColumn {
                items(shuffledQuestions) { question ->
                    QuizQuestionItem(
                        question = question,
                        selectedAnswer = selectedAnswers[question.id],
                        // Al seleccionar una respuesta, guardamos la respuesta seleccionada
                        // para esa pregunta en el diccionario de respuestas seleccionadas
                        // La clave del diccionario es el id de la pregunta, el valor es la respuesta seleccionada
                        onAnswerSelected = { answer ->
                            selectedAnswers = selectedAnswers.toMutableMap().apply {
                                this[question.id!!] = answer
                            }
                        }
                    )
                }
            }

        }
    }
}

// Esta función nos con un progress bar las preguntas contestadas, hay un botón para enviar las respuestas
// y se muestra el resultado del quiz
@Composable
fun AnswerStatus(answeredQuestions: Int, shuffledQuestions: List<QuestionModel>, selectedAnswers: Map<String, String>) {
    var score by remember { mutableStateOf<Int?>(null) }

    // Animación para cambiar el color de la progress bar según el número de preguntas contestadas
    val colorProgressBar by animateColorAsState(
        targetValue = if (answeredQuestions == 10) Color.Green else if (answeredQuestions < 5) Color.Red else Color.Blue,
        animationSpec = tween(durationMillis = 500),
        label = "Animación color"
    )

    // Animación para cambiar el tamaño del botón de comprobar respuestas
    // cada vez que se contesta una pregunta (se hace grande y pequeño)
    // Cuando están todas las preguntas contestadas se queda el tamaño más grande
    var lastAnsweredQuestions by remember { mutableStateOf(answeredQuestions) }
    val size by animateDpAsState(
        targetValue = if (answeredQuestions == 10) 100.dp else if (answeredQuestions != lastAnsweredQuestions) 60.dp else 50.dp,
        animationSpec = tween(durationMillis = 500),
        label = "Animación tamaño",
        finishedListener = {
            lastAnsweredQuestions = answeredQuestions
        }
    )

    Column(modifier = Modifier.padding(bottom = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // Progress bar para indicar el número de preguntas contestadas
        // Cambia de color según el número de preguntas contestadas
        LinearProgressIndicator(
            progress = { answeredQuestions / 10f },
            modifier = Modifier.fillMaxWidth(),
            color = colorProgressBar
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier
                    .size(size)
                    .fillMaxSize(),
                onClick = {
                    // Al hacer clic en el botón de enviar, calculamos la puntuación
                    score = calculateScore(shuffledQuestions, selectedAnswers)
                }
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Submit", modifier = Modifier.fillMaxSize())
            }
        }

        // Mostramos la puntuación si ya se ha calculado
        if (score != null) {
            Text("Your Score: $score / 10", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
        }

    }
}


// Muestra una pregunta con sus opciones de respuesta y un radio button para seleccionar la respuesta
// Recibe como parámetros la pregunta, la respuesta seleccionada y la función para
// decidir qué hacer cuando se selecciona una respuesta
@Composable
fun QuizQuestionItem(
    question: QuestionModel,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit
) {
    val options = remember { (question.incorrectAnswers + question.correctAnswer).shuffled() }

    Card(modifier = Modifier.padding(8.dp)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {
            // Ponemos una imagen como fondo de la pregunta aleatorio y transparente para que se lea
            // bien la pregunta. Ocurre un efecto "raro" cada vez que pulso una respuesta
            // me cambia la imagen de fondo, no sé por qué
            AsyncImage(
                model = backGroundImages.random(),
                contentDescription = "CSS Image",
                modifier = Modifier.fillMaxSize().alpha(0.2f),
                contentScale = ContentScale.FillBounds
            )
            // Mostramos la pregunta y las opciones de respuesta (Radio button)
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = question.question, style = MaterialTheme.typography.headlineSmall)
                options.forEach { option ->
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        RadioButton(
                            selected = option == selectedAnswer,
                            // Pasamos la función para saber qué hacer cuando se selecciona una respuesta
                            // Simplemente la añadimos al diccionario de respuestas seleccionadas
                            onClick = { onAnswerSelected(option) }
                        )
                        Text(text = option, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}

// Para calcular la puntuación, comparamos las respuestas seleccionadas con las respuestas correctas
// y devolvemos el número de respuestas correctas
fun calculateScore(questions: List<QuestionModel>, selectedAnswers: Map<String, String>): Int {
    var score = 0
    for (question in questions) {
        if (selectedAnswers[question.id] == question.correctAnswer) {
            score++
        }
    }
    return score
}
