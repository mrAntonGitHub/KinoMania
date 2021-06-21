package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("countries")
    val countries: List<Country>,
    @SerializedName("description")
    val description: String,
    @SerializedName("distributorRelease")
    val distributorRelease: String,
    @SerializedName("distributors")
    val distributors: String,
    @SerializedName("facts")
    val facts: List<String>,
    @SerializedName("filmId")
    val filmId: Int,
    @SerializedName("filmLength")
    val filmLength: String?,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("nameEn")
    val nameEn: String,
    @SerializedName("nameRu")
    val nameRu: String,
    @SerializedName("posterUrl")
    val posterUrl: String,
    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String,
    @SerializedName("premiereBluRay")
    val premiereBluRay: String,
    @SerializedName("premiereDigital")
    val premiereDigital: Any,
    @SerializedName("premiereDvd")
    val premiereDvd: String,
    @SerializedName("premiereRu")
    val premiereRu: String,
    @SerializedName("premiereWorld")
    val premiereWorld: String,
    @SerializedName("premiereWorldCountry")
    val premiereWorldCountry: String,
    @SerializedName("ratingAgeLimits")
    val ratingAgeLimits: Int,
    @SerializedName("ratingMpaa")
    val ratingMpaa: String,
    @SerializedName("seasons")
    val seasons: List<Any>,
    @SerializedName("slogan")
    val slogan: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("webUrl")
    val webUrl: String,
    @SerializedName("year")
    val year: String
)