package uhk.palecek.chess.api

import com.google.gson.JsonObject
import cz.uhk.fim.cryptoapp.api.SignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uhk.palecek.chess.data.GameData
import uhk.palecek.chess.data.GamesData

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

    @GET(value = "/api/matchHistory")
    suspend fun getMatchHistory(
        @Query("userId") userId: String,
        @Query("page") page: Int,
        @Header("Authorization") token: String
    ): Response<ChessResponse<List<JsonObject>>>

    @GET(value = "/api/games")
    suspend fun getGames(
        @Header("Authorization") token: String
    ): Response<GamesData>

    @GET(value = "/api/room/{roomId}")
    suspend fun isValid(
        @Path("roomId") roomId: String,
        @Header("Authorization") token: String
    ): Response<GameData>

    @GET(value = "/api/user")
    suspend fun getUser(
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<JsonObject>
}