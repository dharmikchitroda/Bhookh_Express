import java.util.Locale

data class InternetData(
    val id: Int,
    val title: String? = null,      // "Chicken", "Seafood" etc
    val image: String? = null, // image URL
) {
    val itemPrice: Int
        get() = (id % 150) + 50
}

data class RecipeResponse(
    val results: List<InternetData>  // "results" — Spoonacular ka JSON key
)