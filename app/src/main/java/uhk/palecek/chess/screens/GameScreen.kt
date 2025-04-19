package uhk.palecek.chess.screens

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.bhlangonijr.chesslib.Square
import com.google.gson.Gson
import io.socket.client.Ack
import org.w3c.dom.Text
import uhk.palecek.chess.components.BoardComponent
import uhk.palecek.chess.consts.Routes
import uhk.palecek.chess.data.PlayerData
import uhk.palecek.chess.data.RoomData
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
            mSocket.close()
            SocketHandler.closeConnection()
        }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        if(players.size < 2)
            Text("Waiting for other player!")
        else
            BoardComponent(side, id, players, navController)
    }
}

