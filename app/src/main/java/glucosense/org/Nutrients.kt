package glucosense.org

import com.squareup.moshi.Json

data class Nutrients(
        @Json(name = "nutrient_id") val nutrient_id: String,
        @Json(name = "nutrient") val nutrient: String,
        @Json(name = "unit") val unit: String,
        @Json(name = "value") val value: String,
        @Json(name = "gm") val gm: String) {}