package com.example.csslearn

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Pantalla de inicio. Contiene una lazycolumn con enlaces a las diferentes pantallas / screens
@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            // No muestro la primera pantalla/screen (porque es el home)
            items(screens.subList(1, screens.size)) { page ->
                HomePageItem(page = page, navController = navController)
            }
        }
    }
}

// Cada uno de los enlaces a las diferentes pantallas / screens
// Le pasamos como parámetro el objeto con la información de la pantalla
// y el navController para poder navegar a la pantalla correspondiente
@Composable
fun HomePageItem(page: PageData, navController: NavController) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable { navController.navigate(page.route) }
    ) {
        Column {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = page.title,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = page.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(8.dp))
        }
    }
}



