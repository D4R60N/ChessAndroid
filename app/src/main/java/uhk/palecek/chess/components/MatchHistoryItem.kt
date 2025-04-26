package uhk.palecek.chess.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uhk.palecek.chess.data.MatchHistory
import uhk.palecek.chess.ui.theme.DarkSquareColor
import uhk.palecek.chess.ui.theme.DrawColor
import uhk.palecek.chess.ui.theme.LoseColor
import uhk.palecek.chess.ui.theme.WinColor
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun MatchHistoryItem(matchHistory: MatchHistory, username: String) {
    val format = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
    val date = LocalDateTime.ofInstant(matchHistory.date, ZoneId.of("Europe/Berlin"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .background(
                    color = if (matchHistory.winner == null) DrawColor else if (matchHistory.winner == username) WinColor else LoseColor
                )
                .border(BorderStroke(5.dp, DarkSquareColor))
                .padding(10.dp)
        ) {
            Text("${matchHistory.players[0]} vs. ${matchHistory.players[1]}")
            Text(if (matchHistory.winner != null) "Winner: ${matchHistory.winner}" else "Draw!")
            Text(date.format(format))
        }
    }

}