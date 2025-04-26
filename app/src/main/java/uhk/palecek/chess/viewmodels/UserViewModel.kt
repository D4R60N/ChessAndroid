package uhk.palecek.chess.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import uhk.palecek.chess.api.ChessApi
import uhk.palecek.chess.data.PlayerData
import uhk.palecek.chess.data.SignData
import uhk.palecek.chess.data.User
import uhk.palecek.chess.data.UserData
import uhk.palecek.chess.repository.UserRepository
import java.time.Instant
import java.util.Base64


class UserViewModel(private val chessApi: ChessApi, private val userRepository: UserRepository) :
    ViewModel() {
    private val _username: MutableStateFlow<String> = MutableStateFlow<String>("")
    private val _token: MutableStateFlow<String?> = MutableStateFlow<String?>(null)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)

    val username: StateFlow<String> = _username.asStateFlow()
    val token: StateFlow<String?> = _token.asStateFlow()
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onTokenChange(newToken: String) {
        _token.value = newToken
    }

    fun isSignIn(): Boolean {
        return _authState.value is AuthState.Authenticated
    }

    fun createPlayerData(): PlayerData {
        val decoded = decodeToken(token.value!!)
        val exp = JSONObject(decoded).getString("exp").toLong()
        val iat = JSONObject(decoded).getString("iat").toLong()
        val id = JSONObject(decoded).getString("id")
        return PlayerData(id, username.value, iat, exp, "")
    }

    fun checkAuthState() {
        viewModelScope.launch {
            if (token.value == null) {
                val user = userRepository.getUserData()
                if (user == null) {
                    _authState.value = AuthState.Unauthenticated
                } else {
                    _token.value = user.token
                    _username.value = user.username
                    resolveToken(user.token)
                }
            } else {
                resolveToken(token.value!!)
            }
        }
    }

    fun resolveToken(token: String) {
        val decoded = decodeToken(token)
        val expiration = JSONObject(decoded).getString("exp")
        val instant = Instant.ofEpochSecond(expiration.toLong())
        if (Instant.now().isBefore(instant)) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun signIn(username: String, password: String) {
        val signData = SignData(User(username, password))
        viewModelScope.launch {
            try {
                val response = chessApi.signIn(signData)
                if (response.isSuccessful) {
                    _username.value = username
                    _token.value = response.body()!!.token
                    _authState.value = AuthState.Authenticated
                    userRepository.setUserData(UserData(0, response.body()!!.token, username))
                } else {
                    _token.value = null
                    _authState.value = AuthState.Error(response.message())
                }
            } catch (e: Exception) {
                _token.value = null
                _authState.value = AuthState.Error(e.message!!)
            }
        }
    }

    fun signUp(username: String, password: String) {
        val signData = SignData(User(username, password))
        viewModelScope.launch {
            try {
                val response = chessApi.signUp(signData)
                if (response.code() == 201) {
                    signIn(username, password)
                } else {
                    _authState.value = AuthState.Error(response.message())
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message!!)
            }
        }
    }

    fun signOut() {
        _username.value = ""
        _token.value = null
        _authState.value = AuthState.Unauthenticated
        viewModelScope.launch {
            userRepository.removeUserData()
        }
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

private fun decodeToken(jwt: String): String {
    val parts = jwt.split(".")
    return try {
        val charset = charset("UTF-8")
        val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
        val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
        "$header"
        "$payload"
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}