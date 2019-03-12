package com.example.pocemployee.repo.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    private var client: Retrofit? = null


    fun getRetrofitClient(url: String): Retrofit? {

        if(client == null)
            client = Retrofit.Builder().baseUrl(url).addConverterFactory(MoshiConverterFactory.create()).build()

        return client

    }


}
