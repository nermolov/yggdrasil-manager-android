package eu.neilalexander.yggdrasil

import android.content.Context
import mobile.Mobile
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object ConfigurationProxy {
    private lateinit var json: JSONObject
    private lateinit var jsonString: String

    operator fun invoke(applicationContext: Context): ConfigurationProxy {
        jsonString = applicationContext
            .resources
            .openRawResource(R.raw.yggdrasil)
            .bufferedReader().use { it.readText() }

        // parse and fix config
        fix()
        return this
    }

    fun resetJSON() {
        val conf = Mobile.generateConfigJSON()
        jsonString = String(conf)
        fix()
    }

    fun resetKeys() {
        val newJson = JSONObject(String(Mobile.generateConfigJSON()))
        updateJSON { json ->
            json.put("PrivateKey", newJson.getString("PrivateKey"))
        }
    }

    fun setKeys(privateKey: String) {
        updateJSON { json ->
            json.put("PrivateKey", privateKey)
        }
    }

    fun updateJSON(fn: (JSONObject) -> Unit) {
        json = JSONObject(jsonString)
        fn(json)
        jsonString = json.toString()
    }

    private fun fix() {
        updateJSON { json ->
            json.put("AdminListen", "none")
            json.put("IfName", "none")
            json.put("IfMTU", 65535)

            val ar = JSONArray()
            ar.put(0, JSONObject("""
                {
                    "Regex": ".*",
                    "Beacon": true,
                    "Listen": true,
                    "Password": ""
                }
            """.trimIndent()))
            json.put("MulticastInterfaces", ar)
        }
    }

    fun getJSON(): JSONObject {
        fix()
        return json
    }

    fun getJSONByteArray(): ByteArray {
        return json.toString().toByteArray(Charsets.UTF_8)
    }

    var multicastListen: Boolean
        get() = (json.getJSONArray("MulticastInterfaces").get(0) as JSONObject).getBoolean("Listen")
        set(value) {
            updateJSON { json ->
                (json.getJSONArray("MulticastInterfaces").get(0) as JSONObject).put("Listen", value)
            }
        }

    var multicastBeacon: Boolean
        get() = (json.getJSONArray("MulticastInterfaces").get(0) as JSONObject).getBoolean("Beacon")
        set(value) {
            updateJSON { json ->
                (json.getJSONArray("MulticastInterfaces").get(0) as JSONObject).put("Beacon", value)
            }
        }

    var multicastPassword: String
        get() = (json.getJSONArray("MulticastInterfaces").get(0) as JSONObject).optString("Password")
        set(value) {
            updateJSON { json ->
                (json.getJSONArray("MulticastInterfaces").get(0) as JSONObject).put("Password", value)
            }
        }
}