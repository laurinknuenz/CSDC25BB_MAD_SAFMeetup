package at.csdc25bb.mad.safmeetup.data.entity.team

import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity

data class Team (
    val id: String,
    val name: String,
    val typeOfSport: String,
    val manager: TeamUser,
    val inviteCode: String,
    val members: List<TeamUser>,
    val pendingMembers: List<TeamUser>?,
    val activities: List<TeamActivity>?
)