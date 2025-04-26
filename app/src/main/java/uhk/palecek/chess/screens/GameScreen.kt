package uhk.palecek.chess.screens

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.bhlangonijr.chesslib.Square
import com.google.gson.Gson
import io.socket.client.Ack
import org.w3c.dom.Text
import uhk.palecek.chess.components.BoardComponent
import uhk.palecek.chess.components.Header
import uhk.palecek.chess.consts.Routes
import uhk.palecek.chess.data.PlayerData
import uhk.palecek.chess.data.RoomData
import uhk.palecek.chess.ui.theme.DarkSquareColor
import uhk.palecek.chess.utils.SocketHandler

@Composable
fun GameScreen(
    navController: NavController,
    side: String,
    id: String
) {
    var players: List<PlayerData> by remember { mutableStateOf<List<PlayerData>>(emptyList()) }
    val mSocket = SocketHandler.getSocket()
    LaunchedEffect(Unit) {
        val gson = Gson()
        mSocket.emit("validateRoom", id, Ack { args ->
            val valid = args[0] as Boolean
            if (!valid) {
                Handler(Looper.getMainLooper()).post {
                    navController.navigate(Routes.Home)
                }
            }
        })
        mSocket.on("opponentJoined") { args ->
            Log.d("room", args[0].toString())
            val playerList = gson.fromJson(args[0].toString(), RoomData::class.java)
            Handler(Looper.getMainLooper()).post {
                players = playerList.players
            }
        }
        if (side == "black") {
            mSocket.emit("joinRoom", id, Ack { args ->
                Log.d("room", args[0].toString())
                val playerList = gson.fromJson(args[0].toString(), RoomData::class.java)
                Handler(Looper.getMainLooper()).post {
                    players = playerList.players
                }
            })
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mSocket.off("opponentJoined")
            mSocket.off("playerDisconnected")
            mSocket.off("endGame")
            mSocket.off("move")
            mSocket.off("joinRoom")
            mSocket.disconnect()
            mSocket.close()
            SocketHandler.closeConnection()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (players.size < 2) {
            Spacer(modifier = Modifier.height(16.dp))
            Header("Waiting for other player!")
            FloatingActionButton(
                containerColor = DarkSquareColor,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = {
                    val mSocket = SocketHandler.getSocket()
                    mSocket.disconnect()
                    mSocket.close()

                    navController.navigate(Routes.Home)
                }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
            }
        } else
            BoardComponent(side, id, players, navController)
    }
}

