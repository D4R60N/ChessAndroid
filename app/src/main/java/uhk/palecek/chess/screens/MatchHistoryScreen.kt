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
import uhk.palecek.chess.api.ApiResult
import uhk.palecek.chess.components.BoardComponent
import uhk.palecek.chess.components.MatchHistoryItem
import uhk.palecek.chess.consts.Routes
import kotlin.coroutines.coroutineContext

@Composable
fun MatchHistoryScreen(
    navController: NavController,
) {
    val matchList by viewModel.cryptoList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCryptoList()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Match History")
        Spacer(modifier = Modifier.height(16.dp))
        when (matchList) {
            is ApiResult.Loading -> {
                // Zobraz indikátor načítání
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            is ApiResult.Success -> {
                // Zobraz data
                val cryptoList = (matchList as ApiResult.Success).data
                LazyColumn(state = listState) {
                    items(matchList) { crypto ->
                        MatchHistoryItem(
                            "a", "b"
                        )
                    }
                }
            }

            is ApiResult.Error -> {
                // Zobraz chybovou hlášku
                val errorMessage = (matchList as ApiResult.Error).message
                Text(text = "Error: $errorMessage")
            }
        }
    }
    }
}

