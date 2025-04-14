package uhk.palecek.chess.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uhk.palecek.chess.api.ApiResult
import uhk.palecek.chess.api.ChessApi
import uhk.palecek.chess.data.MatchHistory

class MatchHistoryViewModel(private val chessApi: ChessApi): ViewModel() {
    private val _matchHistoryList = MutableStateFlow<ApiResult<List<MatchHistory>>>(ApiResult.Loading)
    val cryptoList: StateFlow<ApiResult<List<MatchHistory>>> = _matchHistoryList.asStateFlow()


    fun getCryptoList() {
        viewModelScope.launch {
            _matchHistoryList.value = ApiResult.Loading
            try {
                val response = chessApi.getMatchHistory()
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if(data != null){
                        _matchHistoryList.value = ApiResult.Success(data)
                        Log.d("CryptoViewModel", "getCryptoList: ${response.body()?.data}")
                    }else{
                        _matchHistoryList.value = ApiResult.Error("Data is null")
                        Log.e("CryptoViewModel", "Data is null")
                    }
                } else {
                    _matchHistoryList.value = ApiResult.Error("Error fetching crypto list: ${response.message()}")
                    Log.e("CryptoViewModel", "Error fetching crypto list: ${response.message()}")
                }
            } catch (e: Exception) {
                _matchHistoryList.value = ApiResult.Error("Exception fetching crypto list: ${e.message}")
                Log.e("CryptoViewModel", "Exception fetching crypto list: ${e.message}")
            }
        }
    }
}