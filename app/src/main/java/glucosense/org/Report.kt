package glucosense.org

import com.beust.klaxon.Json

data class Report(
        val sr: String,
        val groups: String,
        val subset: String,
        val end: String,
        val start: String,
        val total: String,
        @Json(name = "foods") val food: Food
)