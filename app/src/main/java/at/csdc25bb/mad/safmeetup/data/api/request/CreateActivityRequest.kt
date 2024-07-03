package at.csdc25bb.mad.safmeetup.data.api.request

data class CreateActivityRequest (
    var subject: String,
    var activityType: String,
    var team: String,
    var opponent: String,
    var location: String,

)