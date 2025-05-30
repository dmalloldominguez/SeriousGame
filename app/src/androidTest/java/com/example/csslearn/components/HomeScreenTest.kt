package com.example.csslearn.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.csslearn.HomeScreen
import com.example.csslearn.screens
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    // Regla para probar componentes de Compose
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Test para comprobar que están todos los elementos de la pantalla home
    @Test
    fun testHomeScreenItems() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(navController = navController)
        }

        screens.subList(1, screens.size).forEach { page ->
            composeTestRule.onNodeWithText(page.title).assertExists()
        }
    }

}