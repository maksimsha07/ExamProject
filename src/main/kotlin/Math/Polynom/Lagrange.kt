package Math.Polynom

class Lagrange(private val points: MutableMap<Double,Double>) : Polynom(){
    var ind : MutableMap<Double,Double> = mutableMapOf()
    init{
        for(i in 0..points.size-1){
            ind.put(points.keys.elementAt(i),points.values.elementAt(i))
        }
        val p = Polynom()
        ind.forEach {
            val r = fundamental(it.key)
            if (r != null) p += r * it.value
            else return@forEach
        }
        coeff = p.coeff
    }

    private fun fundamental(key: Double): Polynom? {
        var p: Polynom = Polynom(1.0)
        ind.forEach {
            if (it.key.compareTo(key)!=0){
                val m = Polynom(-it.key, 1.0) /
                        (key - it.key)
                if (m != null){
                    p = p * m
                } else return@fundamental null
            }
        }
        return p
    }


}
