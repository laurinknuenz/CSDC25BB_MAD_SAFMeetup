package at.csdc25bb.mad.safmeetup.data.api.request

data class UserTeamJoinRequest(
    val userId: String,
    val inviteCode: String,
)