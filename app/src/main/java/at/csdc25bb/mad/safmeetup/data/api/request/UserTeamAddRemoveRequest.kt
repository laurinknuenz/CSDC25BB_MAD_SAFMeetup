package at.csdc25bb.mad.safmeetup.data.api.request

data class UserTeamAddRemoveRequest(
    val userId: String,
    val teamName: String,
)