package at.csdc25bb.mad.safmeetup.data.entity.team

data class Team (
    val id: String = "",
    val name: String = "",
    val typeOfSport: String = "",
    val manager: TeamUser = TeamUser(),
    val inviteCode: String = "",
    val members: List<TeamUser> = emptyList(),
    val pendingMembers: List<TeamUser>? = emptyList(),
    val activities: List<TeamActivity>? = emptyList()
)
