package ru.mendel.apps.zeropatient

import ru.mendel.apps.zeropatient.actors.FieldMathActor
import ru.mendel.apps.zeropatient.geometry.Point
import ru.mendel.apps.zeropatient.models.DiseaseModel
import kotlin.random.Random

object GameHelper {

    fun generatePointsMath(n: Int): ArrayList<Point>{
        //n - количествопациентов
        val allPoints = arrayListOf<Point>()
        val startX = 0f
        val startY = 0f
        val step = 30f
        val rows = (FieldMathActor.HEIGHT/step).toInt()
        val columns = (FieldMathActor.WIDTH/step).toInt()
        var y = startY
        for (i in 0 until rows){
            var x = startX
            for (j in 0 until columns){
                val dx = 4-Random.nextInt(9)
                val dy = 4-Random.nextInt(9)
                val point = Point(x+step/2+dx, y+step/2+dy)
                allPoints.add(point)
                x+=step
            }
            y+=step
        }
        val res = arrayListOf<Point>()
        for (i in 0 until n){
            val pos = Random.nextInt(allPoints.size)
            res.add(allPoints[pos])
            allPoints.removeAt(pos)
        }
        return res
    }


    fun isDead(diseaseModel: DiseaseModel, inHospital: Boolean):Boolean{
        if (inHospital){
            for (i in 0 until diseaseModel.hospitalLevel){
                val p = Random.nextInt(100)
                if(p>=diseaseModel.mortality){
                    return false
                }
            }
            return true
        }else{
            val p = Random.nextInt(100)
            return p<diseaseModel.mortality
        }
    }
}