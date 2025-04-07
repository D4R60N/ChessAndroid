package cz.uhk.fim.cryptoapp

import cz.uhk.fim.cryptoapp.viewmodels.UserViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { UserViewModel() }
}


val networkModule = module {
    single { provideOkHttpClient() }
//    single { provideRetrofit(get()) }
//    single { provideCryptoApi(get()) }
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

//fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//    return Retrofit.Builder()
//        .baseUrl("https://api.coincap.io/v2/")
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//}

//fun provideCryptoApi(retrofit: Retrofit): CryptoApi {
//    return retrofit.create(CryptoApi::class.java) //zde jse Retrofitu předali naše rozhraní pro API, které Retrofit bude implementovat
//}