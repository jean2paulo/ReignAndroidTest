package com.jeanpaulo.reignandroidtest.datasource.local

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.jeanpaulo.buscador_itunes.datasource.Repository
import com.jeanpaulo.buscador_itunes.datasource.local.Database
import com.jeanpaulo.buscador_itunes.datasource.remote.service.IHackerNewsService
import com.jeanpaulo.buscador_itunes.datasource.remote.service.RetrofitServiceFactory
import com.jeanpaulo.reignandroidtest.datasource.IRepository
import com.jeanpaulo.reignandroidtest.datasource.remote.IRemoteDataSource
import com.jeanpaulo.reignandroidtest.datasource.remote.RemoteDataSource

object ServiceLocator {
    private val lock = Any()
    private var database: Database? = null

    @Volatile
    var musicRepository: IRepository? = null
        @VisibleForTesting set

    fun provideMusicRepository(context: Context): IRepository {
        synchronized(this) {
            return musicRepository
                ?: musicRepository
                ?: createMusicRepository(
                    context
                )
        }
    }

    private fun createMusicRepository(context: Context): IRepository {
        val newRepo =
            Repository(
                createRepository(),
                createLocalDataSource(
                    context
                )
            )
        musicRepository = newRepo
        return newRepo
    }

    private fun createRepository(): IRemoteDataSource {
        RetrofitServiceFactory().build()
        val service = RetrofitServiceFactory.retrofit.create(IHackerNewsService::class.java)
        return RemoteDataSource(service)
    }

    private fun createLocalDataSource(context: Context): LocalDataSource {
        val database = database
            ?: createDataBase(
                context
            )
        return LocalDataSource(
            database.hitDao()
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