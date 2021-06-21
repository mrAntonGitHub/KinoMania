package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class FilmVideos(
    @SerializedName("teasers")
    val teasers: List<Teaser>,
    @SerializedName("trailers")
    val trailers: List<Trailer>
)