package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("ratingAwait")
    val ratingAwait: String,
    @SerializedName("ratingAwaitCount")
    val ratingAwaitCount: Int,
    @SerializedName("ratingFilmCritics")
    val ratingFilmCritics: String,
    @SerializedName("ratingFilmCriticsVoteCount")
    val ratingFilmCriticsVoteCount: Int,
    @SerializedName("ratingImdb")
    val ratingImdb: Double,
    @SerializedName("ratingImdbVoteCount")
    val ratingImdbVoteCount: Int,
    @SerializedName("ratingRfCritics")
    val ratingRfCritics: String,
    @SerializedName("ratingRfCriticsVoteCount")
    val ratingRfCriticsVoteCount: Int,
    @SerializedName("ratingVoteCount")
    val ratingVoteCount: Int
)