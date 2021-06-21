package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class ExternalId(
    @SerializedName("imdbId")
    val imdbId: String
)