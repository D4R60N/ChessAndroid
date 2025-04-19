package uhk.palecek.chess.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uhk.palecek.chess.api.ApiResult
import uhk.palecek.chess.api.ChessApi
import uhk.palecek.chess.data.GameData
import uhk.palecek.chess.data.GamesData

class GamesViewModel(private val chessApi: ChessApi) : ViewModel() {
    private val _gameList =
        MutableStateFlow<ApiResult<GamesData>>(ApiResult.Loading)
    val gameList: StateFlow<ApiResult<GamesData>> = _gameList.asStateFlow()

    fun removeGame(roomId: String) {
        if (_gameList.value::class == ApiResult.Success::class) {
            val games = (_gameList.value as ApiResult.Success).data.games
            _gameList.value = ApiResult.Success(GamesData(games.filterNot { it.roomId == roomId }))
        }
    }

    fun getGameList(token: String) {
        viewModelScope.launch {
            _gameList.value = ApiResult.Loading
            try {
                val response = chessApi.getGames("Bearer $token")
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _gameList.value = ApiResult.Success(data)
                    } else {
                        _gameList.value = ApiResult.Error("No data")
                    }
                } else {
                    _gameList.value =
                        ApiResult.Error("Error fetching games: ${response.message()}")
                }
            } catch (e: Exception) {
                _gameList.value =
                    ApiResult.Error("Error fetching games: ${e.message}")
            }
        }
    }
    fun validateRoom(
        roomId: String,
        token: String,
        onResult: (Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = try {
                val response = chessApi.isValid(roomId, "Bearer $token")
                response.isSuccessful && response.body()?.open == true
            } catch (e: Exception) {
                false
            }
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
}