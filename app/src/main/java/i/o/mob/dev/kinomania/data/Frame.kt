package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Frame(
    @SerializedName("image")
    val image: String,
    @SerializedName("preview")
    val preview: String
)