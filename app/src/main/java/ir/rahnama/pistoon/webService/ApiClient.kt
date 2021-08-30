package ir.rahnama.pistoon.webService

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object ApiClient {



    private var retrofit : Retrofit? = null

    private var gson = GsonBuilder()
        .setLenient()
        .create()


    fun getApiClient(url:String): Retrofit? {
        if (retrofit == null){
            retrofit = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
       return retrofit
    }






}