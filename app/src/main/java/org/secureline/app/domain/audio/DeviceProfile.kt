package org.secureline.app.domain.audio

import org.json.JSONObject

data class DeviceProfile(
    val deviceModel: String,
    val sampleRateHz: Int = 8000,
    val modemGain: Double = 1.0,
    val preferSpeakerphone: Boolean = true,
    val disableAec: Boolean = true,
) {
    fun toJson(): String =
        JSONObject()
            .put("deviceModel", deviceModel)
            .put("sampleRateHz", sampleRateHz)
            .put("modemGain", modemGain)
            .put("preferSpeakerphone", preferSpeakerphone)
            .put("disableAec", disableAec)
            .toString()

    companion object {
        fun fromJson(raw: String): DeviceProfile {
            val o = JSONObject(raw)
            return DeviceProfile(
                deviceModel = o.getString("deviceModel"),
                sampleRateHz = o.optInt("sampleRateHz", 8000),
                modemGain = o.optDouble("modemGain", 1.0),
                preferSpeakerphone = o.optBoolean("preferSpeakerphone", true),
                disableAec = o.optBoolean("disableAec", true),
            )
        }
    }
}

class DeviceProfileStore {
    private val byModel = LinkedHashMap<String, DeviceProfile>()

    fun save(profile: DeviceProfile) {
        byModel[profile.deviceModel] = profile
    }

    fun load(deviceModel: String): DeviceProfile? = byModel[deviceModel]

    fun exportJson(deviceModel: String): String? = byModel[deviceModel]?.toJson()

    fun importJson(raw: String): DeviceProfile {
        val profile = DeviceProfile.fromJson(raw)
        save(profile)
        return profile
    }
}
