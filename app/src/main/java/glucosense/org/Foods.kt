package glucosense.org

import com.squareup.moshi.Json
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "foods")
data class Foods (
    @Attribute val nddno: String,
    @Attribute val name: String,
    @Attribute val weight: String,
    @Attribute val measure: String,
    @Element var nutrients: Nutrients
)
/*data class Foods(
        @Json(name = "ndbno") val ndbno: String,
        @Json(name = "name") val name: String,
        @Json(name = "weight") val weight: String,
        @Json(name = "measure") val measure: String,
        @Json(name = "nutrients") val nutrients: List<Nutrients>
) {}*/