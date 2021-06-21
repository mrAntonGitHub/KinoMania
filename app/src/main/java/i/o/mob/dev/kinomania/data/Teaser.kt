package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Teaser(
    @SerializedName("name")
    val name: String,
    @SerializedName("site")
    val site: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
)