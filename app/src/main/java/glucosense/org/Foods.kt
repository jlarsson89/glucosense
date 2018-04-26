package glucosense.org

import com.squareup.moshi.Json

data class Foods(
        @Json(name = "ndbno") val ndbno: String,
        @Json(name = "name") val name: String,
        @Json(name = "weight") val weight: String,
        @Json(name = "measure") val measure: String,
        @Json(name = "nutrients") val nutrients: List<Nutrients>
) {}