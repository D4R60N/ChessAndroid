package uhk.palecek.chess.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uhk.palecek.chess.data.GameData

@Composable
fun GameBrick(gameData: GameData, onClick: () -> Unit) {

    Button(onClick = onClick) {
        Column{
            Text("Player: ${gameData.players[0].username}")
            Text("RoomId: ${gameData.roomId}")
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}