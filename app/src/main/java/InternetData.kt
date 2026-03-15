import com.google.gson.annotations.SerializedName

data class InternetData (

    @SerializedName("stringResourceId")
    val name: String,

    @SerializedName("itemCategoryId")
    val categoryName: String,

    @SerializedName("itemQuantity")
    val quantity: String,

    @SerializedName("item_price")
    val itemPrice: Int,

    @SerializedName("imageResourceId")
    val imageSource: String
)
