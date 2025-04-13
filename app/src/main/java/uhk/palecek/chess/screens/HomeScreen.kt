package uhk.palecek.chess.screens

import android.os.Handler
import android.os.Looper
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
import com.google.gson.Gson
import io.socket.client.Ack
import io.socket.emitter.Emitter
import uhk.palecek.chess.components.BoardComponent
import uhk.palecek.chess.consts.Routes
import uhk.palecek.chess.data.UserData
import uhk.palecek.chess.utils.SocketHandler
import uhk.palecek.chess.viewmodels.UserViewModel
import kotlin.coroutines.coroutineContext

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: UserViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            if (viewModel.isSignIn() && viewModel.token.value != null) {
  //              navController.navigate("game/white/dsa")
                SocketHandler.setSocket()
                SocketHandler.establishConnection()
                val mSocket = SocketHandler.getSocket()
                mSocket.open()
                mSocket.emit("player", viewModel.createPlayerData().toJson())
                mSocket.emit("createRoom", Ack { args ->
                    val roomId = args[0] as String
                    Handler(Looper.getMainLooper()).post {
                        navController.navigate(Routes.game("white", roomId))
                    }
                })
            }
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




