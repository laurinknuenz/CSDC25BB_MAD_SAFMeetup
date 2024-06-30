package at.csdc25bb.mad.safmeetup.data.entity

data class Team (
    val id: String,
    val name: String,
    val type: String,
    val manager: String,
    val inviteCode: String,
    val members: List<String>,
    val activities: List<String>
)