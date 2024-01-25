sealed class ResultState<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T?) : ResultState<T>(data)
    class Error<T>(message: String? = null, data: T? = null) : ResultState<T>(data, message)
    class Loading<T>(data: T? = null) : ResultState<T>(data)
}