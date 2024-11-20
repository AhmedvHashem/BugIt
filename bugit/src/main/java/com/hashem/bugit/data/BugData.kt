package com.hashem.bugit.data

import com.hashem.bugit.domain.Bug

data class BugData(val imageUrl: String, val fields: Map<String, String>)

internal fun BugData.toBug(): Bug {
    return Bug(
        imageUrl = imageUrl,
        fields = fields,
    )
}