package at.csdc25bb.mad.safmeetup.data.api.request

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
    val password: String,
    val inviteCode: String
)