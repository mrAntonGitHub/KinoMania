package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class GenreFilter(
    @SerializedName("genre")
    val genre: String,
    @SerializedName("id")
    val id: Int
)