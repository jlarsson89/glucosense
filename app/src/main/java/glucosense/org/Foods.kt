package glucosense.org

data class Foods(
    val ndbno: String,
    val name: String,
    val weight: String,
    val measure: String,
    val nutrients: List<Nutrients>
) {}