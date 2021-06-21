package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class CountryFilter(
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: Int
)