package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class Filter(
    @SerializedName("countries")
    val countries: List<CountryFilter>,
    @SerializedName("genres")
    val genres: List<GenreFilter>
)