package Math.Polynom


class Newton(private val points: MutableMap<Double,Double>): Polynom(){
    var ind : MutableMap<Double,Double> = mutableMapOf()
    var l = Polynom(1.0)
    init{
        for(i in 0..points.size-1){
            ind.put(points.keys.elementAt(i),points.values.elementAt(i))
        }
        var pol = Polynom(points.values.elementAt(0))

        for(i in 1..ind.size-1){
            l = l * Polynom(-ind.keys.elementAt(i-1),1.0)
            pol = pol +  l *  DividedDifferent(i)
        }
        coeff = pol.coeff
    }

    fun DividedDifferent(k:Int) : Double
    {
        var f = 0.0
        for(i in 0..k){
            var z = 1.0
            for(j in 0..k){
                if(j!=i){
                    z *=(ind.keys.elementAt(i)-ind.keys.elementAt(j))
                }
            }
            f += ind.values.elementAt(i)/z
        }
        return f
    }
    fun add(point :MutableMap<Double,Double>){
        var pol = Polynom()
        pol.coeff = this.coeff
        for(i in 0..point.size-1){
            ind.put(point.keys.elementAt(i),point.values.elementAt(i))
        }
        l = l * Polynom(-ind.keys.elementAt(ind.size-2),1.0)
        pol = pol +  l * DividedDifferent(ind.size-1)
        this.coeff = pol.coeff
    }

}