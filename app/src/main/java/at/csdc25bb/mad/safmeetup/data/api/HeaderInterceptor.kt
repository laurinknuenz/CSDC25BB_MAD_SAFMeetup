package at.csdc25bb.mad.safmeetup.data.api

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val sharedPref: SharedPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var cookie = sharedPref.getString("Cookie", "")

        val newReq = cookie?.let { request.newBuilder().addHeader("Cookie", it).build() }  ?: run { request }

        val response = chain.proceed(newReq)

        cookie = response.headers["Set-Cookie"]
        if (cookie != null) {
            with(sharedPref.edit()) {
                putString("Cookie", cookie)
                apply()
            }
        }

        return response
    }
}