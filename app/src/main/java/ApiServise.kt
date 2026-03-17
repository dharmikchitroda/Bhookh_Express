import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://training-uploads.internshala.com/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface FlashApiServise {
    @GET("android/grocery_delivery_app/items.json")
    suspend fun getItems(): List<InternetData> // Ye function time le sakta hai Ye coroutine me hi chalega Ye main thread block nahi karega

}

object FlashApi {
    val retrofitService: FlashApiServise by lazy { // inherite karyo interface ne
        retrofit.create(
            FlashApiServise::class.java
        )
    }
}
