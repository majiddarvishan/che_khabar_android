package ir.rezarasuolzadeh.takhfif.di

import com.readystatesoftware.chuck.ChuckInterceptor
import ir.rezarasuolzadeh.takhfif.api.AdvertiserDao
import ir.rezarasuolzadeh.takhfif.api.UserDao
import ir.rezarasuolzadeh.takhfif.service.repository.AdvertiserRepository
import ir.rezarasuolzadeh.takhfif.service.repository.UserRepository
import android.content.Context
import ir.rezarasuolzadeh.takhfif.viewmodel.AdvertiserViewModel
import ir.rezarasuolzadeh.takhfif.viewmodel.UserViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// network module
val networkModule = module {
    factory { provideHttpInterceptor() }
    factory { okHttpClientProvider(get(), androidContext()) }
    factory { retrofitProvider(get()) }
}

// map module
val mapPageModule = module(override = true) {
    viewModel { AdvertiserViewModel(get()) }
    single { AdvertiserRepository(get()) }
    single { advertiserDaoProvider(get()) }
}

// profile module
val infoPageModule = module(override = true) {
    viewModel { UserViewModel(get()) }
    single { UserRepository(get()) }
    single { userDaoProvider(get()) }
}

// user dao provider
fun userDaoProvider(retrofit: Retrofit): UserDao = retrofit.create(UserDao::class.java)

// user dao provider
fun advertiserDaoProvider(retrofit: Retrofit): AdvertiserDao = retrofit.create(AdvertiserDao::class.java)

// network providers
fun retrofitProvider(httpClient: OkHttpClient): Retrofit {
    val url = "http://majiddarvishan.pythonanywhere.com/"
    return Retrofit
        .Builder()
        .baseUrl(url)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

// interceptor provider
fun provideHttpInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
}

// client provider
fun okHttpClientProvider(interceptor: HttpLoggingInterceptor, context: Context): OkHttpClient {
    return OkHttpClient().newBuilder()
        .addInterceptor(interceptor)
        .addInterceptor(ChuckInterceptor(context))
        .retryOnConnectionFailure(true)
        .build()
}