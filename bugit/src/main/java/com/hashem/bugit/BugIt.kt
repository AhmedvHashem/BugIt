package com.hashem.bugit

import com.hashem.bugit.data.BugitDataSource
import com.hashem.bugit.framework.GoogleSheetDataSource

class BugIt private constructor(
    fields: MutableMap<String, String> = mutableMapOf(),
    externalConnector: BugitDataSource
) {
    class Builder {
        object CONST {
            const val KEY_SCREENSHOT = "bug-screenshot"
            const val KEY_DESCRIPTION = "bug-description"
        }


        private val fields = mutableMapOf<String, String>()
        private var externalConnector: BugitDataSource? = null

        fun screenShot(screenShot: String) =
            apply { this.fields[CONST.KEY_SCREENSHOT] = screenShot }

        fun description(description: String) =
            apply { this.fields[CONST.KEY_DESCRIPTION] = description }

        fun addExtraField(key: String, defaultValue: String) =
            apply { this.fields[key] = defaultValue }

        fun useExternalConnector(externalConnector: BugitDataSource) = apply {
            this.externalConnector = externalConnector
        }

        fun build() = BugIt(fields, externalConnector ?: GoogleSheetDataSource())
    }

    fun show() {

    }
}