package com.jeanpaulo.buscador_itunes.datasource.remote.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class RetrofitServiceFactory {

    companion object {
        val BASE_URL = "https://hn.algolia.com/api/v1/"
        lateinit var retrofit: Retrofit
            private set
    }

    fun build() {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory
                    .create(
                        Moshi
                            .Builder()
                            //DATE
                            .add(Date::class.java, Rfc3339DateJsonAdapter() )
                            .addLast(KotlinJsonAdapterFactory())
                            .build()
                    )
            )
            .build()
    }
}