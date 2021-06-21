package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Budget(
    @SerializedName("budget")
    val budget: String,
    @SerializedName("grossRu")
    val grossRu: Int,
    @SerializedName("grossUsa")
    val grossUsa: Int,
    @SerializedName("grossWorld")
    val grossWorld: Int,
    @SerializedName("marketing")
    val marketing: Int
)