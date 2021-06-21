package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class FilmStaff(
    @SerializedName("description")
    val description: String?,
    @SerializedName("nameEn")
    val nameEn: String?,
    @SerializedName("nameRu")
    val nameRu: String?,
    @SerializedName("posterUrl")
    val posterUrl: String?,
    @SerializedName("professionKey")
    val professionKey: String?,
    @SerializedName("professionText")
    val professionText: String?,
    @SerializedName("staffId")
    val staffId: Int
)