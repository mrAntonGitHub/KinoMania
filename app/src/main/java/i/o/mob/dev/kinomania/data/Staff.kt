package i.o.mob.dev.kinomania.data

import com.google.gson.annotations.SerializedName

data class Staff(
    @SerializedName("age")
    val age: Int?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("birthplace")
    val birthplace: String?,
    @SerializedName("death")
    val death: String?,
    @SerializedName("deathplace")
    val deathplace: Any?,
    @SerializedName("facts")
    val facts: List<String>?,
    @SerializedName("films")
    var films: List<StaffFilm>?,
    @SerializedName("growth")
    val growth: Int?,
    @SerializedName("hasAwards")
    val hasAwards: Int?,
    @SerializedName("nameEn")
    val nameEn: String?,
    @SerializedName("nameRu")
    val nameRu: String?,
    @SerializedName("personId")
    val personId: Int,
    @SerializedName("posterUrl")
    val posterUrl: String?,
    @SerializedName("profession")
    val profession: String?,
    @SerializedName("sex")
    val sex: String?,
    @SerializedName("spouses")
    val spouses: List<Spouse>?,
    @SerializedName("webUrl")
    val webUrl: String?
)