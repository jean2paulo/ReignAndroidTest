package com.jeanpaulo.reignandroidtest.datasource.local

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.jeanpaulo.buscador_itunes.datasource.local.Database
import com.jeanpaulo.buscador_itunes.datasource.remote.service.IHackerNewsService
import com.jeanpaulo.buscador_itunes.datasource.remote.service.RetrofitServiceFactory
import com.jeanpaulo.reignandroidtest.datasource.remote.RemoteDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.RemoteDataSourceImpl

object ServiceLocator {
    private val lock = Any()
    private var database: Database? = null

    @Volatile
    var musicRepository: com.jeanpaulo.reignandroidtest.datasource.Repository? = null
        @VisibleForTesting set

    fun provideMusicRepository(context: Context): com.jeanpaulo.reignandroidtest.datasource.Repository {
        synchronized(this) {
            return musicRepository
                ?: musicRepository
                ?: createMusicRepository(
                    context
                )
        }
    }

    private fun createMusicRepository(context: Context): com.jeanpaulo.reignandroidtest.datasource.Repository {
        val newRepo =
            com.jeanpaulo.buscador_itunes.datasource.RepositoryImpl(
                createRepository(),
                createLocalDataSource(
                    context
                )
            )
        musicRepository = newRepo
        return newRepo
    }

    private fun createRepository(): RemoteDataSource {
        RetrofitServiceFactory().build()
        val service = RetrofitServiceFactory.retrofit.create(IHackerNewsService::class.java)
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
            Database::class.java, "Hit.db"
        ).build()
        database = result
        return result
    }
}