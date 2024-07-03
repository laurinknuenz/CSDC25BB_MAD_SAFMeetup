package at.csdc25bb.mad.safmeetup.data.api.request

data class UpdateActivityRequest (
    var activityId: String,
    var guestUserId: String,
    var attendance: Boolean

)