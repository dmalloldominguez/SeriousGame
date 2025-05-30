package com.example.csslearn

import QuestionViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.csslearn.ui.theme.CSSLearnTheme
import kotlinx.coroutines.launch

// MainActivity.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon

/*data class GridItem(
    val id: Int,
    val color: String,
    val iconName: String,
    val classes: List<String>
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CssSelectorGame()
        }
    }
}

@Composable
fun CssSelectorGame() {
    var items by remember { mutableStateOf(generateItems()) }
    var selectedIndices by remember { mutableStateOf(setOf<Int>()) }
    var matchedIndices by remember { mutableStateOf(setOf<Int>()) }
    var correctSelector by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var resultText by remember { mutableStateOf("") }
    var showCorrectSelector by remember { mutableStateOf(false) }

    fun generateScenario() {
        items = generateItems()
        selectedIndices = generateSelection(items)
        correctSelector = calculateCorrectSelector(selectedIndices, items)
        matchedIndices = emptySet()
        userInput = TextFieldValue("")
        resultText = ""
        showCorrectSelector = false
    }

    LaunchedEffect(Unit) {
        generateScenario()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        for (row in 0 until 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    val item = items[index]
                    val isSelected = selectedIndices.contains(index)
                    val isMatched = matchedIndices.contains(index)

                    ItemBox(item, isSelected, isMatched)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Escribe un selector CSS (ej. .red.home, .blue: first-child):")

        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val selector = userInput.text.trim()
            val matches = applySelector(selector, items)
            matchedIndices = matches
            resultText = if (matches == selectedIndices) "¡Correcto!" else "Incorrecto. Intenta de nuevo."
        }) {
            Text("Validar Selector")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { generateScenario() }) {
            Text("Nuevo escenario")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showCorrectSelector = true }) {
            Text("Mostrar selector correcto")
        }

        if (showCorrectSelector) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Selector correcto: $correctSelector")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(resultText)
    }
}

@Composable
fun ItemBox(item: GridItem, selected: Boolean, matched: Boolean) {
    val icon: ImageVector = when (item.iconName) {
        "home" -> Icons.Default.Home
        "heart" -> Icons.Default.Favorite
        "person" -> Icons.Default.Person
        "phone" -> Icons.Default.Phone
        else -> Icons.Default.Home
    }

    val iconColor = when (item.color) {
        "red" -> Color.Red
        "blue" -> Color.Blue
        "green" -> Color.Green
        else -> Color.Gray
    }

    val borderColor = when {
        selected -> Color.Yellow
        matched -> Color.Magenta
        else -> Color.Transparent
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val scaleModifier = if (selected) Modifier.scale(scale) else Modifier

    Box(
        modifier = Modifier
            .size(80.dp)
            .border(4.dp, borderColor)
            .then(scaleModifier),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = item.iconName,
            tint = iconColor,
            modifier = Modifier.size(48.dp)
        )
    }
}

fun generateItems(): List<GridItem> {
    val icons = listOf("home", "heart", "person", "phone")
    val colors = listOf("red", "blue", "green")

    return (0 until 9).map { i ->
        val color = colors.random()
        val iconName = icons.random()
        GridItem(
            id = i,
            color = color,
            iconName = iconName,
            classes = listOf(color, iconName)
        )
    }
}

fun generateSelection(items: List<GridItem>): Set<Int> {
    val mode = listOf("color", "icon", "position").random()

    return when (mode) {
        "color" -> {
            val color = items.random().color
            items.mapIndexedNotNull { i, item -> if (item.color == color) i else null }.toSet()
        }
        "icon" -> {
            val iconName = items.random().iconName
            items.mapIndexedNotNull { i, item -> if (item.iconName == iconName) i else null }.toSet()
        }
        "position" -> {
            val color = items.random().color
            val index = items.indexOfFirst { it.color == color }
            if (index != -1) setOf(index) else emptySet()
        }
        else -> emptySet()
    }
}

fun applySelector(selector: String, items: List<GridItem>): Set<Int> {
    val parts = selector.trim().split('.').filter { it.isNotEmpty() }
    if (parts.isEmpty()) return emptySet()

    val classSelectors = mutableListOf<String>()
    var pseudoClass: String? = null

    for (part in parts) {
        if (part.contains(":")) {
            val (cls, pseudo) = part.split(":", limit = 2)
            classSelectors.add(cls)
            pseudoClass = pseudo
        } else {
            classSelectors.add(part)
        }
    }

    val filtered = items.mapIndexedNotNull { i, item ->
        val hasAllClasses = classSelectors.all { it in item.classes }
        if (hasAllClasses) i else null
    }.toSet()

    val finalSet = when (pseudoClass) {
        "first-child" -> filtered.filter { it == 0 }.toSet()
        "last-child" -> filtered.filter { it == items.lastIndex }.toSet()
        null -> filtered
        else -> emptySet()
    }

    return finalSet
}

fun calculateCorrectSelector(selectedIndices: Set<Int>, items: List<GridItem>): String {
    if (selectedIndices.isEmpty()) return ""

    val selectedItems = selectedIndices.map { items[it] }

    if (selectedIndices.size == 1) {
        val idx = selectedIndices.first()
        if (idx == 0) return ".${selectedItems.first().color}:first-child"
        if (idx == items.lastIndex) return ".${selectedItems.first().color}:last-child"
    }

    val allSameColor = selectedItems.all { it.color == selectedItems.first().color }
    val allSameIcon = selectedItems.all { it.iconName == selectedItems.first().iconName }

    if (allSameColor && allSameIcon) return ".${selectedItems.first().color}.${selectedItems.first().iconName}"
    if (allSameColor) return ".${selectedItems.first().color}"
    if (allSameIcon) return ".${selectedItems.first().iconName}"
    if (selectedIndices.size == 1) return ".${selectedItems.first().color}"

    return "(sin selector único)"
}
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSSLearnTheme {
                val navController = rememberNavController()
                CSSLearnScaffold(navController = navController)
            }
        }
    }
}

// Definimos en el Scaffold el drawer, topbar y bottombar
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CSSLearnScaffold(navController: NavHostController) {
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    // El Drawer contendrá información del nombre de usuario / editable por el usuario
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Serious Game", style = MaterialTheme.typography.titleLarge)
                    Image(
                        painter = painterResource(id = R.drawable.css_icon),
                        contentDescription = "Icon",
                        modifier = Modifier.size(150.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("User:")
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                        )

                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Serious Game") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    // Botón para abrir el drawer
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    // Enlaces a las diferentes pantallas desde el menú derecho de la TopBar
                    actions = {
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                for (screen in screens) {
                                    DropdownMenuItem(text = { Text(screen.title) }, onClick = { navController.navigate(screen.route); expanded = false })
                                }
                            }
                        }
                    }
                )
            },
            // La Bottom bar contendrá información del usuario loggeado
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    if (username.isNotEmpty())
                        Text("Logged in as $username", modifier = Modifier.padding(16.dp))
                    else {
                        Text("Not logged in", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        ) {
            // El contenido que se muestra (las screens) lo gestionamos con el NavHost.
            // Por defecto, mostramos la pantalla home
            paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("quiz") {
                        QuizScreen(QuestionViewModel())
                    }
                    composable("management") {
                        QuestionManagementScreen(QuestionViewModel())
                    }
                    composable("cssselectors") {
                        CSSSelectorScreen()
                    }
                }
            }
        }
    }
}
