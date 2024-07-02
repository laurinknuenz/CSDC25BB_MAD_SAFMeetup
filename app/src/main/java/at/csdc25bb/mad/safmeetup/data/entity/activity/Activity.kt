package at.csdc25bb.mad.safmeetup.data.entity.activity

data class Activity(
    val _id: String,
    val subject: String,
    val hostingTeam: ActivityTeam,
    val type: ActivityType,
    val opponent: ActivityTeam,
//    val date: Date,
    val location: String,
    val listOfGuests: List<ActivityGuest>?
)