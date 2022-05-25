package and5.abrar.estafet.network

import and5.abrar.estafet.model.GetUserItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("user")
    fun allUser(
        @Query("username") username :String
    ): Call<List<GetUserItem>>
}