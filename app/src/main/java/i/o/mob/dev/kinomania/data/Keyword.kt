package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Keyword(
    @SerializedName("films")
    val films: MutableList<FilmByKeyword>,
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("pagesCount")
    val pagesCount: Int,
    @SerializedName("searchFilmsCountResult")
    val searchFilmsCountResult: Int
)