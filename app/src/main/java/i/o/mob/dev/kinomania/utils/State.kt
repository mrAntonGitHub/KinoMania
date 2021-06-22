package i.o.mob.dev.kinomania.utils

sealed class State<T> {
    class Loading<T> : State<T>()

    class Success<T>(val data: T) : State<T>()

    class Error<T>(val exception: Exception?, val message: String? = null) : State<T>()

    class NoMoreData<T>(val data: T) : State<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success<T>(data)
        fun <T> error(exception: Exception?, message: String? = null) = Error<T>(exception, message)
        fun <T> noMoreData(data: T) = NoMoreData<T>(data)
    }
}