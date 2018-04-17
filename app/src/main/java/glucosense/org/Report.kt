package glucosense.org

import java.lang.reflect.Array

class Reports {
    data class Report (
        val sr: String,
        val groups: String,
        val subset: String,
        val end: String,
        val start: String,
        val total: String,
        val foods: Array
    )
}