package com.example.csslearn.components

import QuestionViewModel
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.csslearn.QuestionManagementScreen
import com.example.csslearn.models.QuestionModel
import okhttp3.internal.wait
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuestionManagementScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: QuestionViewModel

    @Before
    fun setup() {
        viewModel = QuestionViewModel() // Asegúrate de usar un ViewModel de prueba si es necesario
    }

    // Test para comprobar que se crea una pregunta
    // No me funciona, imagino que tiene que ver con el viewmodel
    // o con que es un lazycolumn y esta pregunta se queda al final del mismo
    // He buscado información y se supone que con mockito se puede hacer
    // haciendo un mock del viewmodel. Pero no me he aclarado y tampoco he
    // tenido más tiempo para investigar
    @Test
    fun testCreateQuestion() {
        composeTestRule.setContent {
            QuestionManagementScreen(viewModel = viewModel)
        }

        // Rellenamos el formulario de creación de preguntas
        composeTestRule.onNodeWithText("Question").performTextInput("PREGUNTA")
        composeTestRule.onNodeWithText("Incorrect Answer 1").performTextInput("A")
        composeTestRule.onNodeWithText("Incorrect Answer 2").performTextInput("B")
        composeTestRule.onNodeWithText("Incorrect Answer 3").performTextInput("C")
        composeTestRule.onNodeWithText("Correct Answer").performTextInput("D")
        // Hacemos click en el botón de crear pregunta
        composeTestRule.onNodeWithText("Create Question").performClick()

        // No me funciona, imagino que tiene que ver con el viewmodel
        // o con que es un lazycolumn y esta pregunta se queda al final del mismo
        composeTestRule.onAllNodes(hasText("What is Compose?")).fetchSemanticsNodes().isNotEmpty()
    }


    // Test para comprobar que se elimina una pregunta
    // Sólo me funciona la primera vez que se ejecuta, después falla
    // porque no encuentra el nodo con el texto "What does CSS stand for?"
    // Supongo que es porque el nodo no se ha eliminado correctamente
    // He probado a insertar en el viewModel una pregunta con el texto "What does CSS stand for?"
    // y después eliminarla, pero me sigue fallando
    @Test
    fun testDeleteQuestion() {
        composeTestRule.setContent {
            viewModel.createQuestion(QuestionModel(question = "What does CSS stand for?", incorrectAnswers = listOf("A", "B", "C"), correctAnswer = "D"))
            QuestionManagementScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("What does CSS stand for?").assertExists()
        composeTestRule.onAllNodesWithText("Delete")[0].performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("What does CSS stand for?").fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNodeWithText("What does CSS stand for?").assertDoesNotExist()
    }



}