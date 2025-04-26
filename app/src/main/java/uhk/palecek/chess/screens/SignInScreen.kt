package uhk.palecek.chess.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import uhk.palecek.chess.components.SignInComponent
import uhk.palecek.chess.viewmodels.UserViewModel


@Composable
fun SignInScreen(
    viewModel: UserViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SignInComponent(viewModel)
    }
}

