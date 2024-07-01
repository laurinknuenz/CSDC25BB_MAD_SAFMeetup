package at.csdc25bb.mad.safmeetup.data.utils

sealed class ResourceState<T> {
    class Idle<T> : ResourceState<T>()
    class Loading<T> : ResourceState<T>()
    data class Success<T> (val data: T) : ResourceState<T>()
    data class Error<T> (val error: String) : ResourceState<T>()

}