package com.hashem.bugit.framework

import com.hashem.bugit.Utils
import com.hashem.bugit.data.BugData
import com.hashem.bugit.data.BugDataSource
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

internal class GoogleSheetBugDataSource : BugDataSource {

    override suspend fun report(imagePath: String, fields: Map<String, String>): BugData {

        val payload = JSONObject()
            .put("image", Utils.getFileBase64String(imagePath))
            .put("imageMimeType", "image/jpeg")
            .put("fields", JSONArray(fields.map { it.value }))
            .toString()
        var connection: HttpURLConnection? = null
        try {
            val url =
                URL("https://script.google.com/macros/s/AKfycbxA1__FJ0WTGweQXxhjKS1CCjveVpT2TXdiiCUuQabUkGqAtp3HM-cNZdPXzcxCRixi/exec")
            connection = url.openConnection() as HttpURLConnection
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
                    val jsonData = JSONObject(it.readText())
                    val isSuccess = jsonData.getBoolean("success")
                    if (isSuccess) {
                        BugData(
                            jsonData.getString("data"),
                            fields
                        )
                    } else
                        throw Exception(jsonData.getString("error"))
                }
            } else {
                throw Exception("Failed to report bug")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to report bug")
        } finally {
            connection?.disconnect()
        }
    }
}