package at.csdc25bb.mad.safmeetup.data.entity.team

import at.csdc25bb.mad.safmeetup.data.entity.activity.ActivityTeam
import at.csdc25bb.mad.safmeetup.data.entity.activity.ActivityType

data class TeamActivity(
    val _id: String,
    val subject: String,
    val hostingTeam: ActivityTeam,
    val type: ActivityType,
    val opponent: ActivityTeam,
    val location: String,
)