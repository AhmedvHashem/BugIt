package com.hashem.bugit.framework

import android.util.Log
import com.hashem.bugit.Utils
import com.hashem.bugit.data.BugItDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

internal class GoogleSheetDataSource : BugItDataSource {
    override suspend fun report(imagePath: String, fields: Map<String, String>): JSONObject {
        Log.e("GoogleSheetDataSource", "imagePath: $imagePath, fields: $fields")
        Log.e("GoogleSheetDataSource", "Thread name: ${Thread.currentThread().name}")

        val imageBase64 = Utils.getFileBase64String(imagePath)

        val payload = JSONObject()
            .put("image", imageBase64)
            .put("imageMimeType", "image/jpeg")
            .put("fields", JSONArray(fields.map { it.value }))
            .toString()
        var connection: HttpURLConnection? = null
        try {
            val url =
                URL("https://script.google.com/macros/s/AKfycbxA1__FJ0WTGweQXxhjKS1CCjveVpT2TXdiiCUuQabUkGqAtp3HM-cNZdPXzcxCRixi/exec")
            connection = withContext(Dispatchers.IO) {
                url.openConnection()
            } as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")

            // Handle the request
            connection.outputStream.use { outputStream: OutputStream ->
                outputStream.write(payload.toByteArray(Charsets.UTF_8))
            }

            // Handle the response
            val responseCode = connection.responseCode
            return if (responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use {
                    JSONObject(it.readText())
                }
            } else {
                JSONObject(
                    mapOf(
                        "success" to false,
                        "error" to "Failed to report bug"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return JSONObject(
                mapOf(
                    "success" to false,
                    "error" to e.message
                )
            )
        } finally {
            connection?.disconnect()
        }
    }
}