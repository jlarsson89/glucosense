package glucosense.org

import com.beust.klaxon.Json

data class Food(
        val ndbno: String,
        val name: String,
        val weight: String,
        val measure: String,
        @Json(name = "nutrients") val nutrient: Nutrient
)