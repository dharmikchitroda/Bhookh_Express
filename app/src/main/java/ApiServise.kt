import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.spoonacular.com/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface FlashApiServise {
    // 1. Home screen - saari categories
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("apiKey") apiKey: String = "edd76cc0c8224a989d11bbd6879d0669",
        @Query("query") query: String? = null,
        @Query("number") number: Int = 30
    ): RecipeResponse// Ye function time le sakta hai Ye coroutine me hi chalega Ye main thread block nahi karega
}

object FlashApi {
    val retrofitService: FlashApiServise by lazy { // inherite karyo interface ne
        retrofit.create(FlashApiServise::class.java)
    }
}
