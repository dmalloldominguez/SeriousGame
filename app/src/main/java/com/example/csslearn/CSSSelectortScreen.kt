package com.example.csslearn

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val colors = listOf("red", "blue", "green")
val tags = listOf("home", "heart", "person", "phone")

data class CssItem(
    val id: Int,
    val colorClass: String,
    val tag: String,
    val classList: List<String>
)

@Composable
fun CSSSelectorScreen() {
    var itemList = remember { mutableStateListOf<CssItem>() }
    var correctSelector by remember { mutableStateOf("") }
    var selectedIndices by remember { mutableStateOf(setOf<Int>()) }
    var matchedIndices by remember { mutableStateOf(setOf<Int>()) }
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var showWinDialog by remember { mutableStateOf(false) }

    fun createNewScenario() {
        itemList.clear()
        for (i in 0 until 9) {
            val color = colors.random()
            val tag = tags.random()
            val classes = listOf(color)
            itemList.add(CssItem(i, color, tag, classes))
        }

        val selectionMode = listOf("color", "tag").random()
        val selected = mutableSetOf<Int>()

        if (selectionMode == "color") {
            val color = itemList.random().colorClass
            for (i in itemList.indices) {
                if (itemList[i].colorClass == color) selected.add(i)
            }
            correctSelector = ".${color}"
        } else if (selectionMode == "tag") {
            val tag = itemList.random().tag
            for (i in itemList.indices) {
                if (itemList[i].tag == tag) selected.add(i)
            }
            correctSelector = tag
        }

        selectedIndices = selected
        matchedIndices = emptySet()
        userInput = TextFieldValue("")
    }

    LaunchedEffect(Unit) {
        createNewScenario()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("CSS Selector Game", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        for (row in 0 until 3) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    if (index < itemList.size) {
                        CssItemBox(
                            item = itemList[index],
                            isSelected = selectedIndices.contains(index),
                            isMatched = matchedIndices.contains(index)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Escribe un selector CSS (`home.red`, `person.green`):")
        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val selector = userInput.text.trim()
                val result = applySelector(selector, itemList)
                matchedIndices = result
                val isCorrect = result == selectedIndices
                showWinDialog = isCorrect
            }) {
                Icon(Icons.Default.Check, contentDescription = "Comprobar")
                Spacer(modifier = Modifier.width(4.dp))

            }

            Button(onClick = { createNewScenario() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Nuevo")
                Spacer(modifier = Modifier.width(4.dp))

            }

            Button(onClick = {
                userInput = TextFieldValue(correctSelector)
            }) {
                Icon(Icons.Default.Search, contentDescription = "Solución")
                Spacer(modifier = Modifier.width(4.dp))

            }
        }

        if (showWinDialog) {
            AlertDialog(
                onDismissRequest = { showWinDialog = false },
                title = { Text("¡Bien!", fontSize = 22.sp) },
                text = { Text("Has conseguido seleccionar todos los elementos.") },
                confirmButton = {
                    Button(onClick = { showWinDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun CssItemBox(item: CssItem, isSelected: Boolean, isMatched: Boolean) {
    val icon: ImageVector = when (item.tag) {
        "home" -> Icons.Default.Home
        "heart" -> Icons.Default.Favorite
        "person" -> Icons.Default.Person
        "phone" -> Icons.Default.Phone
        else -> Icons.Default.Home
    }

    val iconColor = when (item.colorClass) {
        "red" -> Color.Red
        "blue" -> Color.Blue
        "green" -> Color(0xFF006400)
        else -> Color.Red
    }

    val scale = if (isSelected) {
        val infiniteTransition = rememberInfiniteTransition()
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulsing"
        ).value
    } else {
        1f
    }

    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isMatched) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.LightGray, shape = CircleShape)
            )
        }
        Icon(
            imageVector = icon,
            contentDescription = item.tag,
            tint = iconColor,
            modifier = Modifier
                .scale(scale)
                .size(60.dp)
        )
    }
}

fun applySelector(selector: String, items: List<CssItem>): Set<Int> {
    val parts = selector.trim().split(".")
    val tag = parts.getOrNull(0) ?: ""
    val classes = if (parts.size > 1) parts.subList(1, parts.size) else emptyList()
    val matched = mutableSetOf<Int>()
    for (i in items.indices) {
        val item = items[i]
        val tagMatches = tag.isEmpty() || item.tag == tag
        val classesMatch = classes.all { it in item.classList }
        if (tagMatches && classesMatch) {
            matched.add(i)
        }
    }
    return matched
}
