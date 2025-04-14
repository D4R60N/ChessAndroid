package cz.uhk.fim.cryptoapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uhk.palecek.chess.api.ChessApi
import uhk.palecek.chess.viewmodels.MatchHistoryViewModel
import uhk.palecek.chess.viewmodels.UserViewModel

val viewModelModule = module {
    viewModel { UserViewModel(get()) }
    viewModel { MatchHistoryViewModel(get()) }
}


val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideCryptoApi(get()) }
}

//val helperModule = module { //todo
//    single { NotificationHelper(androidContext()) }
//}

fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideCryptoApi(retrofit: Retrofit): ChessApi {
    return retrofit.create(ChessApi::class.java)
}