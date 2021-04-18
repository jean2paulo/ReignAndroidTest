package com.jeanpaulo.reignandroidtest.datasource

import android.content.Context
import androidx.room.Room
import com.jeanpaulo.buscador_itunes.datasource.RepositoryImpl
import com.jeanpaulo.buscador_itunes.datasource.local.Database
import com.jeanpaulo.buscador_itunes.datasource.remote.service.IHackerNewsService
import com.jeanpaulo.reignandroidtest.datasource.local.LocalDataSourceImpl
import com.jeanpaulo.reignandroidtest.datasource.remote.RemoteDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.RemoteDataSourceImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

object ServiceLocator {
    private val lock = Any()
    private var database: Database? = null

    @Volatile
    var musicRepository: Repository? = null

    fun provideMusicRepository(context: Context): Repository {
        synchronized(this) {
            return musicRepository
                ?: musicRepository
                ?: createHitRepository(
                    context
                )
        }
    }

    private fun createHitRepository(context: Context): Repository {
        val newRepo =
            RepositoryImpl(
                createRemoteDataSource(),
                createLocalDataSource(
                    context
                )
            )
        musicRepository = newRepo
        return newRepo
    }

    private fun createRemoteDataSource(): RemoteDataSource {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory
                    .create(
                        Moshi
                            .Builder()
                            //DATE
                            .add(Date::class.java, Rfc3339DateJsonAdapter())
                            .addLast(KotlinJsonAdapterFactory())
                            .build()
                    )
            )
            .build()
        val service = retrofit.create(IHackerNewsService::class.java)
        return RemoteDataSourceImpl(service)
    }

    private fun createLocalDataSource(context: Context): LocalDataSourceImpl {
        val database = database
            ?: createDataBase(
                context
            )
        return LocalDataSourceImpl(
            database.hitDao(),
            database.garbageDao()
        )
    }

    //ROOM

    private fun createDataBase(context: Context): Database {
        val result = Room.databaseBuilder(
            context.applicationContext,
            Database::class.java, Constants.DATABASE_NAME
        ).build()
        database = result
        return result
    }
}