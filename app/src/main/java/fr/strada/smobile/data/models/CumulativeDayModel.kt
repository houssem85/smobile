package fr.strada.smobile.data.models

import fr.strada.smobile.data.models.pointeuse.TimeModel

class CumulativeDayModel (

    val date: String,
    // added and see if item is expanded or not
    var isExpanded: Boolean = false,
    var time: List<TimeModel>,
    var comment : List<CommentModel>? = null
)