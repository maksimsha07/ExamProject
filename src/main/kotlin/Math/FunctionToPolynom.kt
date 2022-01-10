package Math

import Math.Polynom.Newton
import kotlin.math.pow
import kotlin.math.sin

class FunctionToPolynom(var f: (Double)->Double,private val a:Int,private val b:Int,private val n:Int) {

    private var point:MutableList<Double> = mutableListOf()

    private fun generatepoint(){
        for (i in 0..n-1 ){
            point.add((a+i*((b-a)/n-1)).toDouble())
        }
    }
     fun create():Newton{
        generatepoint()
        var mapp = mutableMapOf<Double,Double>()
        for (i in 0..n-1){
            mapp.put(point.elementAt(i),f(point.elementAt(i)))
        }
        var pol = Newton(mapp)
        return pol
    }

}