package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class FilmFiltered(
    @SerializedName("films")
    val films: MutableList<Film>?,
    @SerializedName("pagesCount")
    val pagesCount: Int?
)