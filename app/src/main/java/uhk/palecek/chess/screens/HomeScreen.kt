package uhk.palecek.chess.screens

import android.graphics.Color
import android.graphics.Color.WHITE
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.socket.client.Ack
import uhk.palecek.chess.R
import uhk.palecek.chess.components.Header
import uhk.palecek.chess.consts.Routes
import uhk.palecek.chess.ui.theme.Black
import uhk.palecek.chess.ui.theme.DarkSquareColor
import uhk.palecek.chess.ui.theme.LightSquareColor
import uhk.palecek.chess.ui.theme.White
import uhk.palecek.chess.utils.SocketHandler
import uhk.palecek.chess.viewmodels.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: UserViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Header("Welcome to chess!")
        Spacer(modifier = Modifier.height(80.dp))
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                modifier = Modifier
                    .clickable {
                        if (viewModel.isSignIn() && viewModel.token.value != null) {
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
                    },
                painter = painterResource(R.drawable.create),
                contentDescription = "Start Game"
            )
            Text(
                "Start New Game", fontSize = 24.sp, color = Black,
                modifier = Modifier.padding(10.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Welcome to Chess App!\n \n" +
                    "Play. Learn. Win.\n \n" +
                    "Your chess journey starts here. ♟\uFE0F \n \n \n" +
                    "This is an student project. \n \n Daniel Paleček - FIM",
            modifier = Modifier
                .fillMaxSize()
                .background(DarkSquareColor)
                .padding(10.dp),
            fontSize = 20.sp,
            color = White,
            fontWeight = FontWeight.SemiBold
        )
    }
}




