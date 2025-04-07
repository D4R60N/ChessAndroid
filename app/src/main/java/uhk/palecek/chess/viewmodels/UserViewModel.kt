package cz.uhk.fim.cryptoapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class UserViewModel() : ViewModel() {
    private val _username: MutableLiveData<String> = MutableLiveData()
    private val _password: MutableLiveData<String> = MutableLiveData()
    private val _token: MutableLiveData<String> = MutableLiveData()

    val username: LiveData<String> = _username
    val password: LiveData<String> = _password
    val token: LiveData<String> = _token

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }
    fun onTokenChange(newToken: String) {
        _token.value = newToken
    }

    fun isSignIn(): Boolean {
        return token.value != null
    }
}