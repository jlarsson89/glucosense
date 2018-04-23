package glucosense.org

data class Report (
    val sr: String,
    val groups: String,
    val subset: String,
    val end: String,
    val start: String,
    val total: String,
    val foods: List<Foods>
) {}