package cz.uhk.fim.cryptoapp

import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uhk.palecek.chess.api.ChessApi
import uhk.palecek.chess.data.MyObjectBox
import uhk.palecek.chess.data.UserData
import uhk.palecek.chess.repository.UserRepository
import uhk.palecek.chess.viewmodels.GamesViewModel
import uhk.palecek.chess.viewmodels.MatchHistoryViewModel
import uhk.palecek.chess.viewmodels.UserViewModel

val repositoryModule = module {
    single { UserRepository(get()) }
}

val viewModelModule = module {
    viewModel { UserViewModel(get(), get()) }
    viewModel { MatchHistoryViewModel(get()) }
    viewModel { GamesViewModel(get()) }
}


val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideCryptoApi(get()) }
}

val objectBoxModule = module {
    single {
        MyObjectBox.builder()
            .androidContext(androidContext())
            .build()
    }
    single { get<BoxStore>().boxFor(UserData::class.java) }
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