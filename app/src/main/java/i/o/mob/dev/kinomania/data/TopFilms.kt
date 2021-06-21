package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class TopFilms(
    @SerializedName("films")
    val films: List<TopFilmItem>,
    @SerializedName("pagesCount")
    val pagesCount: Int
)