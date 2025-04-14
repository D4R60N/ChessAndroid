package uhk.palecek.chess.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import cz.uhk.fim.cryptoapp.api.SignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

import uhk.palecek.chess.data.SignData
import uhk.palecek.chess.data.SignInData
import uhk.palecek.chess.data.UserStatsData

interface ChessApi {
    @POST(value = "/api/register")
    suspend fun signUp(
        @Body() interval: SignData
    ): Response<Unit>

    @POST(value = "/api/login")
    suspend fun signIn(
        @Body() interval: SignData
    ): Response<SignInResponse<SignInData>>

    @GET(value = "/api/matchHistory")
    suspend fun getMatchHistory(
        @Query("userId") userId: String,
        @Query("page") page: Int,
        @Header("Authorization") token: String
    ): Response<ChessResponse<List<JsonObject>>>

    @GET(value = "/api/user")
    suspend fun getUser(
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<JsonObject>
}