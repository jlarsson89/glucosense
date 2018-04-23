package glucosense.org

/*class Nutrient(var id: Long?, var group: NutrientGroup?, var name: String?, var unit: String?) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val nutrient = o as Nutrient?

        if (if (group != null) !group!!.equals(nutrient!!.group) else nutrient!!.group != null) return false
        if (id != nutrient.id) return false
        if (if (name != null) name != nutrient.name else nutrient.name != null) return false
        return if (if (unit != null) unit != nutrient.unit else nutrient.unit != null) false else true

    }

    override fun hashCode(): Int {
        var result = id!!.hashCode()
        result = 31 * result + if (group != null) group!!.hashCode() else 0
        result = 31 * result + if (name != null) name!!.hashCode() else 0
        result = 31 * result + if (unit != null) unit!!.hashCode() else 0
        return result
    }
}*/