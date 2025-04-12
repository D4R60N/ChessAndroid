package uhk.palecek.chess.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uhk.palecek.chess.components.SignUpComponent
import uhk.palecek.chess.viewmodels.UserViewModel


@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: UserViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SignUpComponent(navController, viewModel)
    }
}

