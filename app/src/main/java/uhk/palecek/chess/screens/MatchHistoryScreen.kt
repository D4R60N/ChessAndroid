package uhk.palecek.chess.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import uhk.palecek.chess.api.ApiResult
import uhk.palecek.chess.components.Header
import uhk.palecek.chess.components.MatchHistoryItem
import uhk.palecek.chess.viewmodels.MatchHistoryViewModel
import uhk.palecek.chess.viewmodels.UserViewModel

@Composable
fun MatchHistoryScreen(
    userViewModel: UserViewModel,
    matchHistoryViewModel: MatchHistoryViewModel = koinViewModel<MatchHistoryViewModel>(),
) {
    val username = userViewModel.username.collectAsState()
    val matchHistoryList by matchHistoryViewModel.matchHistoryList.collectAsState()

    LaunchedEffect(Unit) {
        if (userViewModel.isSignIn())
            matchHistoryViewModel.getMatchHistoryList(
                userViewModel.createPlayerData().id.toString(),
                userViewModel.token.value.toString()
            )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Header("Match History")
        Spacer(modifier = Modifier.height(16.dp))
        when (matchHistoryList) {
            is ApiResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is ApiResult.Success -> {
                val matchHistory = (matchHistoryList as ApiResult.Success).data
                LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
                    items(matchHistory, key = { it.date }) { matchHistoryItem ->
                        MatchHistoryItem(matchHistoryItem, username.value)
                    }
                }
            }

            is ApiResult.Error -> {
                // Zobraz chybovou hlášku
                val errorMessage = (matchHistoryList as ApiResult.Error).message
                Text(text = "Error: $errorMessage")
            }
        }
    }
}


