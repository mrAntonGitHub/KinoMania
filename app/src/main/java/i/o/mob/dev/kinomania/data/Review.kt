package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("reviewAutor")
    val reviewAutor: String,
    @SerializedName("reviewData")
    val reviewData: String,
    @SerializedName("reviewDescription")
    val reviewDescription: String,
    @SerializedName("reviewId")
    val reviewId: Int,
    @SerializedName("reviewTitle")
    val reviewTitle: String,
    @SerializedName("reviewType")
    val reviewType: String,
    @SerializedName("userNegativeRating")
    val userNegativeRating: Any,
    @SerializedName("userPositiveRating")
    val userPositiveRating: Any
)