package com.news.newssphere.di

import android.content.Context
import androidx.room.Room
import com.news.newssphere.BuildConfig
import com.news.newssphere.data.remote.api.NewsApi
import com.news.newssphere.data.remote.repository.NewsRepository
import com.news.newssphere.data.room.NewsDao
import com.news.newssphere.data.room.NewsRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NewAppModule {
    @Provides
    @Singleton
    fun provideApi(): NewsApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(api: NewsApi, dao: NewsDao) = NewsRepository(api, dao)

    @Provides
    @Singleton
    fun provideNewsDao(database: NewsRoomDatabase) = database.getDao()

    @Provides
    @Singleton
    fun provideNewsRoomDatabase(@ApplicationContext context: Context): NewsRoomDatabase {
        return Room.databaseBuilder(context, NewsRoomDatabase::class.java, "db_news_articles")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}