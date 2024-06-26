package at.csdc25bb.mad.safmeetup.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun loginUser(username: String, password: String) {
    Log.d("TAG", "------------------- Logging in... -------------------")
    val call = RetrofitInstance.api.loginUser(username, password)

    call.enqueue(object : Callback<Map<String, String>> {
        override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
            if (response.isSuccessful) {
                Log.d("TAG", "------------------------ Response successful! ------------------------")
                response.body()?.let { Log.d("TAG", it.getValue("id")) }
            } else {

                Log.d("TAG", "------------------------ Response not successful! ------------------------")
                response.body()?.let { Log.d("TAG", it.getValue("error")) }
            }
        }

        override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
            print(t.message)
        }
    })
    Log.d("TAG", "------------------- Call enqueued! -------------------")
}