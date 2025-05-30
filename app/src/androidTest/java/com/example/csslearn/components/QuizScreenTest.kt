package com.example.csslearn.components
import QuestionViewModel
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csslearn.QuizScreen
import org.junit.Rule
import org.junit.Test

class QuizScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test para comprobar que se muestra la pregunta y se puede pulsar en las respuestas
    @Test
    fun testQuizScreenAnswerQuestion() {
        composeTestRule.setContent {
            val viewModel = viewModel<QuestionViewModel>()
            QuizScreen(viewModel = viewModel)
        }

        // Pulsa un radio button
        composeTestRule.onAllNodes(hasClickAction())[1].performClick()
        // Comprueba que se ha pulsado el radio button
        composeTestRule.onAllNodes(hasClickAction())[1].assertIsSelected()

    }

    // Test para comprobar que al pulsar el botón de submit se muestra la puntuación
    // No me funciona, no entiedno la razón
    @Test
    fun testQuizScreenSubmit() {
        composeTestRule.setContent {
            val viewModel = viewModel<QuestionViewModel>()
            QuizScreen(viewModel = viewModel)
        }

        // Hago click en el último elemento que tiene la acción de click
        // (siempre es un radio button)
        composeTestRule.onAllNodes(hasClickAction()).onLast().performClick()
        // Hago click en el botón de submit
        composeTestRule.onNodeWithContentDescription("Submit").performClick()
        // Espero a que se cargue la puntuación
        composeTestRule.waitForIdle()
        // Compruebo que se ha cargado la puntuación (veo que se carga el texto "Your Score:")
        // pero  el assert me da error
        // He probado varios parámetros... pero sin éxito
        composeTestRule.onNodeWithText("Your Score:", useUnmergedTree = true, ignoreCase = true).assertExists()
    }

}
