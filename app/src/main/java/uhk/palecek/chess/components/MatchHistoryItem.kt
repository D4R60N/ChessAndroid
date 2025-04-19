package uhk.palecek.chess.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import uhk.palecek.chess.data.MatchHistory
import uhk.palecek.chess.ui.theme.DrawColor
import uhk.palecek.chess.ui.theme.LoseColor
import uhk.palecek.chess.ui.theme.WinColor

@Composable
fun MatchHistoryItem(matchHistory: MatchHistory, username: String) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .background(
                color = if (matchHistory.winner == null) DrawColor else if (matchHistory.winner == username) WinColor else LoseColor
            )
            .padding(10.dp)
    ) {
        Text("${matchHistory.players[0]} vs. ${matchHistory.players[1]}")
        Text(if (matchHistory.winner != null) "Winner: ${matchHistory.winner}" else "Draw!")
        Text(matchHistory.date.toString())
    }
    Spacer(modifier = Modifier.height(8.dp))
}