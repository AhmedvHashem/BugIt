package com.hashem.bugit

import android.content.Context
import android.net.Uri
import com.hashem.bugit.data.BugitDataSource
import com.hashem.bugit.framework.GoogleSheetDataSource
import com.hashem.bugit.ui.BugItActivity

class BugIt private constructor(val config: Config) {

    companion object {
        private var bugIt: BugIt? = null

        fun init(config: Config) = apply {
            bugIt = BugIt(config.build())
        }

        fun getInstance(): BugIt {
            if (bugIt == null) {
                throw IllegalArgumentException("You need to initialize BugIt first in the application class")
            } else {
                return bugIt!!
            }
        }
    }

    class Config(
        val fields: MutableList<String> = mutableListOf("Description"),
        var connector: BugitDataSource = GoogleSheetDataSource()
    ) {
        fun addExtraField(label: String) =
            apply { fields.add(label) }

        fun useExternalConnector(externalConnector: BugitDataSource) =
            apply { connector = externalConnector }

        internal fun build() = Config(fields, connector)
    }

    fun show(context: Context, image: Uri) {
        BugItActivity.start(context, image, config.fields)
    }
}