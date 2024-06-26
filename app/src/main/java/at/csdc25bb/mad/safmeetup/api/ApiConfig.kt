package at.csdc25bb.mad.safmeetup.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

object RetrofitInstance {
    private const val BASE_URL = "http://localhost:5000/api/"

    val api: SfmApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SfmApi::class.java)
    }
}

interface SfmApi {
    @FormUrlEncoded
    @POST("auth/login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<Map<String, String>>
}