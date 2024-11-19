package com.hashem.bugit

import android.content.Context
import android.net.Uri
import com.hashem.bugit.data.BugItDataSource
import com.hashem.bugit.framework.GoogleSheetDataSource
import com.hashem.bugit.ui.BugItActivity

class BugIt private constructor(val config: Config) {

    companion object {
        private var bugIt: BugIt? = null

        fun init(config: Config) = apply {
            bugIt = BugIt(config)
        }

        fun getInstance(): BugIt {
            if (bugIt == null) {
                throw IllegalArgumentException("You need to initialize BugIt first in the application class")
            } else {
                return bugIt!!
            }
        }
    }

    class Config {
        var allowMultipleImage: Boolean = false
            private set

        var fields: MutableMap<String, String> = mutableMapOf("01_description" to "Description")
            private set

        var connector: BugItDataSource = GoogleSheetDataSource()
            private set

        fun allowMultipleImage(enable: Boolean) =
            apply { allowMultipleImage = enable }

        /*
            key: used for sorting also, so it should be in the format ex: 01_description, 02_priority, 03_department
         */
        fun addExtraField(key: String, label: String) =
            apply { fields[key] = label }

        fun useExternalConnector(externalConnector: BugItDataSource) =
            apply { connector = externalConnector }
    }

    fun show(context: Context, image: ArrayList<Uri>) {
        BugItActivity.start(context, image)
    }
}