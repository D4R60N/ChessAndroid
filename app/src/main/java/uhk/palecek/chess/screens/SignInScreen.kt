package uhk.palecek.chess.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uhk.palecek.chess.components.SignInComponent


@Composable
fun SignInScreen(
    navController: NavController,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SignInComponent(navController)
    }
}

