package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class FilmFrame(
    @SerializedName("frames")
    val frames: List<Frame>
)