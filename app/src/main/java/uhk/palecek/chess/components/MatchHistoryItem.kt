package uhk.palecek.chess.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uhk.palecek.chess.data.MatchHistory
import uhk.palecek.chess.ui.theme.Purple40

@Composable
fun MatchHistoryItem(matchHistory: MatchHistory) {


    Column(
//        modifier = Modifier.background(color = )
    ) {
        Text("${matchHistory.players[0]} vs. ${matchHistory.players[1]}")
        Text("Winner: ${matchHistory.winner}")
        Text(matchHistory.date.toString())
        Spacer(modifier = Modifier.height(16.dp))
    }
}