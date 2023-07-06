package com.example.ladbrokes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.ladbrokes.ui.theme.LadbrokesTheme

@Composable
fun LadbrokesApp() {
    LadbrokesTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            LadbokesActions(navController)
        }

        LadbrokesNavGraph(
            navController = navController,
            navigateToHome = navigationActions.navigateToHome,
            navigateToDetail = navigationActions.navigateToDetail
        )
    }
}