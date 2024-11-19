package com.hashem.bugit.framework

import android.util.Log
import com.hashem.bugit.Utils
import com.hashem.bugit.data.BugitDataSource

//https://script.google.com/macros/s/AKfycbxA1__FJ0WTGweQXxhjKS1CCjveVpT2TXdiiCUuQabUkGqAtp3HM-cNZdPXzcxCRixi/exec
internal class GoogleSheetDataSource : BugitDataSource {
    override suspend fun report(image: String, fields: Map<String, String>) {
        Log.e("GoogleSheetDataSource", "screenShot: $image, fields: $fields")
        Log.e("GoogleSheetDataSource", "Thread name: ${Thread.currentThread().name}")

        val imageBase64 = Utils.getFileBase64String(image)
        Log.e("GoogleSheetDataSource", "imageBase64: $imageBase64")

    }
}