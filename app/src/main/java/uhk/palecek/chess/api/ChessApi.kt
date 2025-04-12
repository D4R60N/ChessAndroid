package uhk.palecek.chess.api

import cz.uhk.fim.cryptoapp.api.SignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

import uhk.palecek.chess.data.SignData
import uhk.palecek.chess.data.SignInData

interface ChessApi {
    @POST(value = "/api/register")
    suspend fun signUp(
        @Body() interval: SignData
    ): Response<Unit>

    @POST(value = "/api/login")
    suspend fun signIn(
        @Body() interval: SignData
    ): Response<SignInResponse<SignInData>>
}