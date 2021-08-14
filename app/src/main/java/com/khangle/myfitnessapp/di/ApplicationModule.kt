package com.khangle.myfitnessapp.di

import android.content.Context
import androidx.room.Room
import com.khangle.myfitnessapp.data.db.MyFitnessDB
import com.khangle.myfitnessapp.data.db.MyFitnessDao
import com.khangle.myfitnessapp.data.db.StringListTypeConverter
import com.khangle.myfitnessapp.data.network.MyFitnessAppService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    const val BASE_URL = "https://myfitnessapi.herokuapp.com/"
    const val BASE_URL2 = "http://192.168.1.5:5000/"

    @Provides
    @Singleton
    fun provideClient(@ApplicationContext context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideWebservice(okHttpClient: OkHttpClient): MyFitnessAppService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyFitnessAppService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyFitnessDB(@ApplicationContext context: Context): MyFitnessDB {
        return Room.databaseBuilder(
            context,
            MyFitnessDB::class.java, "myfitnessdb"
        ).addTypeConverter(StringListTypeConverter())
            .build()
    }

    @Provides
    @Singleton
    fun provideMyFitnessDao(db: MyFitnessDB): MyFitnessDao {
        return db.myFitnessDao()
    }

}