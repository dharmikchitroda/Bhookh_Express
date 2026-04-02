import java.util.Locale

data class InternetData(
    val idCategory: String,
    val strCategory: String,      // "Chicken", "Seafood" etc
    val strCategoryThumb: String, // image URL
    val strCategoryDescription: String
)



data class Meal(
    val strMeal: String,        // name
    val strMealThumb: String,   // photo
    val idMeal: String          // id - baad mein detail ke liye
)
