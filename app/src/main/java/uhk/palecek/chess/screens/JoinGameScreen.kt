package uhk.palecek.chess.screens

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.socket.client.Ack
import org.koin.androidx.compose.koinViewModel
import uhk.palecek.chess.api.ApiResult
import uhk.palecek.chess.components.GameBrick
import uhk.palecek.chess.components.Header
import uhk.palecek.chess.components.MatchHistoryItem
import uhk.palecek.chess.consts.Routes
import uhk.palecek.chess.ui.theme.DarkSquareColor
import uhk.palecek.chess.ui.theme.LightSquareColor
import uhk.palecek.chess.utils.SocketHandler
import uhk.palecek.chess.viewmodels.AuthState
import uhk.palecek.chess.viewmodels.GamesViewModel
import uhk.palecek.chess.viewmodels.UserViewModel

@Composable
fun JoinGameScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    gamesViewModel: GamesViewModel = koinViewModel<GamesViewModel>(),
) {
    val listState = rememberLazyListState()
    val gameList by gamesViewModel.gameList.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (userViewModel.isSignIn())
            gamesViewModel.getGameList(userViewModel.token.value.toString())
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            containerColor = DarkSquareColor,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                if (userViewModel.isSignIn())
                    gamesViewModel.getGameList(userViewModel.token.value.toString())
            }) {
            Icon(Icons.Filled.Refresh, contentDescription = "refresh")
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Header("Games")
            Spacer(modifier = Modifier.height(16.dp))
            when (gameList) {
                is ApiResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is ApiResult.Success -> {
                    val games = (gameList as ApiResult.Success).data.games
                    if (games.isEmpty()) {
                        Text("No open games.")
                    } else {
                        LazyColumn(state = listState) {
                            items(games) { gameData ->
                                if (gameData.open == true)
                                    GameBrick(gameData) {
                                        if (userViewModel.isSignIn() && userViewModel.token.value != null) {
                                            gamesViewModel.validateRoom(
                                                gameData.roomId,
                                                userViewModel.token.value.toString()
                                            ) {
                                                if (it) {
                                                    SocketHandler.setSocket()
                                                    SocketHandler.establishConnection()
                                                    val mSocket = SocketHandler.getSocket()
                                                    mSocket.open()
                                                    mSocket.emit(
                                                        "player",
                                                        userViewModel.createPlayerData().toJson()
                                                    )
                                                    navController.navigate(
                                                        Routes.game(
                                                            "black",
                                                            gameData.roomId
                                                        )
                                                    )
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Room is full or no longer open!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    gamesViewModel.removeGame(gameData.roomId)
                                                }
                                            }
                                        }
                                    }
                            }
                        }
                    }
                }

                is ApiResult.Error -> {
                    val errorMessage = (gameList as ApiResult.Error).message
                    Text(text = "Error: $errorMessage")
                }
            }
        }
    }
}

