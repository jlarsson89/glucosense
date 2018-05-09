package glucosense.org

import com.beust.klaxon.Json

data class Response(
        @Json(name = "report") val report: Report
)