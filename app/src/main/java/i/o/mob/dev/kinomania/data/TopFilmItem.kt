package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class TopFilmItem(
    @SerializedName("countries")
    val countries: List<Country>?,
    @SerializedName("filmId")
    val filmId: Int,
    @SerializedName("filmLength")
    val filmLength: String?,
    @SerializedName("genres")
    val genres: List<Genre>?,
    @SerializedName("nameEn")
    val nameEn: String?,
    @SerializedName("nameRu")
    val nameRu: String?,
    @SerializedName("posterUrl")
    val posterUrl: String?,
    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("ratingChange")
    val ratingChange: Any?,
    @SerializedName("ratingVoteCount")
    val ratingVoteCount: Int?,
    @SerializedName("year")
    val year: String?
)