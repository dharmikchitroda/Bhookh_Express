import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"


    private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface FlashApiServise {
    // 1. Home screen - saari categories
    @GET("categories.php")
    suspend fun getCategories(): List<InternetData> // Ye function time le sakta hai Ye coroutine me hi chalega Ye main thread block nahi karega

    @GET("filter.php")         // endpoint
    suspend fun getMealsByCategory(
        @Query("c") category: String   // ?c=Seafood or ?c=Chicken
    ): List<Meal>


}

object FlashApi {
    val retrofitService: FlashApiServise by lazy { // inherite karyo interface ne
        retrofit.create(
            FlashApiServise::class.java
        )
    }
}
