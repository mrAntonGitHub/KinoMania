package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class StaffFilm(
    @SerializedName("description")
    val description: String,
    @SerializedName("filmId")
    val filmId: Int,
    @SerializedName("general")
    val general: Boolean,
    @SerializedName("nameEn")
    val nameEn: String?,
    @SerializedName("nameRu")
    val nameRu: String?,
    @SerializedName("professionKey")
    val professionKey: String,
    @SerializedName("rating")
    val rating: String?
)