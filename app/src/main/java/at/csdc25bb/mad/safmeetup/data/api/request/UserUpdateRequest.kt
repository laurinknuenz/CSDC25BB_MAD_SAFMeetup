package at.csdc25bb.mad.safmeetup.data.api.request

data class UserUpdateRequest (
    val firstname: String = "",
    val lastname: String = "",
    val username: String = "",
    val password: String = "",
    val email: String = ""
)