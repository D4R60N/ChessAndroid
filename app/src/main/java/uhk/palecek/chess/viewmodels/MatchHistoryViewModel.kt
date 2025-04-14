package uhk.palecek.chess.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import uhk.palecek.chess.api.ApiResult
import uhk.palecek.chess.api.ChessApi
import uhk.palecek.chess.data.MatchHistory
import java.time.Instant

class MatchHistoryViewModel(private val chessApi: ChessApi) : ViewModel() {
    private val _matchHistoryList =
        MutableStateFlow<ApiResult<List<MatchHistory>>>(ApiResult.Loading)
    val matchHistoryList: StateFlow<ApiResult<List<MatchHistory>>> = _matchHistoryList.asStateFlow()


    fun getMatchHistoryList(userId: String, token: String) {
        viewModelScope.launch {
            _matchHistoryList.value = ApiResult.Loading
            try {
                val response = chessApi.getMatchHistory(userId, 1, "Bearer $token")
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        val list = mutableListOf<MatchHistory>()
                        data.forEach {
                            val str = it.toString()
                            val playersString = JSONObject(str).getString("players")
                            val players: List<String> =
                                playersString.replace("\"", "").replace("[", "").replace("]", "")
                                    .split(",")
                            var winner: String? = null
                            if (JSONObject(str).has("winner"))
                                winner = JSONObject(str).getString("winner")
                            val date = Instant.parse(JSONObject(str).getString("date").toString())
                            var matchHistory = MatchHistory(players, winner, date)

                            val p = mutableListOf<String>()
                            var w = matchHistory.winner
                            matchHistory.players.forEach {
                                val response2 = chessApi.getUser(it, "Bearer $token")
                                val data2 = response2.body()
                                if (data2 != null) {
                                    val username = JSONObject(data2.toString()).getString("username")
                                    p.add(username)
                                    if (it.equals(matchHistory.winner))
                                        w = username
                                } else {
                                    p.add(it)
                                }
                            }
                            matchHistory = MatchHistory(p, w, matchHistory.date)
                            list.add(matchHistory)
                        }


                        _matchHistoryList.value = ApiResult.Success(list)
                    } else {
                        _matchHistoryList.value = ApiResult.Error("No data")
                    }
                } else {
                    _matchHistoryList.value =
                        ApiResult.Error("Error fetching match history: ${response.message()}")
                }
            } catch (e: Exception) {
                _matchHistoryList.value =
                    ApiResult.Error("Error fetching match history: ${e.message}")
            }
        }
    }
}