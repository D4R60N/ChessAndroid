package uhk.palecek.chess.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.move.Move
import uhk.palecek.chess.components.BoardComponent
import uhk.palecek.chess.consts.Routes
import kotlin.coroutines.coroutineContext

@Composable
fun HomeScreen(
    navController: NavController,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            navController.navigate(Routes.Game)
        }) {
            Text("Start Game")
        }
        Button(onClick = {
            navController.navigate(Routes.JoinGame)
        }) {
            Text("Join Game")
        }
        Button(onClick = {
            navController.navigate(Routes.MatchHistory)
        }) {
            Text("Match History")
        }
        Button(onClick = {
            navController.navigate(Routes.Forum)
        }) {
            Text("Forum")
        }
    }
}

