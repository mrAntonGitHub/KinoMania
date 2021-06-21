package i.o.mob.dev.kinomania.data


import com.google.gson.annotations.SerializedName

data class ReviewValues(
    @SerializedName("ratingGoodReview")
    val ratingGoodReview: String,
    @SerializedName("ratingGoodReviewVoteCount")
    val ratingGoodReviewVoteCount: Int,
    @SerializedName("reviewsCount")
    val reviewsCount: Int
)