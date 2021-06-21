package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class FilmReview(
    @SerializedName("filmId")
    val filmId: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pagesCount")
    val pagesCount: Int,
    @SerializedName("reviewAllCount")
    val reviewAllCount: Int,
    @SerializedName("reviewAllPositiveRatio")
    val reviewAllPositiveRatio: String,
    @SerializedName("reviewNegativeCount")
    val reviewNegativeCount: Int,
    @SerializedName("reviewNeutralCount")
    val reviewNeutralCount: Any,
    @SerializedName("reviewPositiveCount")
    val reviewPositiveCount: Int,
    @SerializedName("reviews")
    val reviews: List<Review>
)