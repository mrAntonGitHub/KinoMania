package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Film(
    @SerializedName("countries")
    val countries: List<Country>,
    @SerializedName("filmId")
    val filmId: Int,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("nameEn")
    val nameEn: String,
    @SerializedName("nameRu")
    val nameRu: String,
    @SerializedName("posterUrl")
    val posterUrl: String,
    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("ratingVoteCount")
    val ratingVoteCount: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("year")
    val year: String
)