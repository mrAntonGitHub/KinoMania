package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class FilmWrapper(
    @SerializedName("budget")
    val budget: Budget,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("externalId")
    val externalId: ExternalId,
    @SerializedName("rating")
    val rating: Rating,
    @SerializedName("review")
    val review: ReviewValues
)