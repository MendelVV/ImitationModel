package ru.mendel.apps.zeropatient

import ru.mendel.apps.zeropatient.models.MathModel
import ru.mendel.apps.zeropatient.models.ResultsModel

object ExperimentHelper {

    fun sortAndRemove(results: ArrayList<MathModel>){
        results.sortBy { it.endDays }
        val end = results.size/10
        for (i in 0 until end){
            results.removeAt(0)
            results.removeAt(results.lastIndex)
        }
    }

    fun sortAndFill(results: ArrayList<MathModel>){
        results.sortBy { it.endDays }
        val max = results.last().endDays
        for (res in results){
            while (res.endDays<=max){
                res.duplicateLast()
            }
        }
    }

    fun createMeans(results: ArrayList<MathModel>): ResultsModel{
        val res = ResultsModel()
        val max = results.maxBy { it.endDays }!!.endDays
        val n = results.size
        res.lastDay = max
        //узнали максимальное число дней
        for (i in 0 until max){//по дням
            var sumC = 0f
            var sumL = 0f
            var sumI = 0f
            var sumIm = 0f
            var sumD = 0f
            var sumV = 0f
            var sumH = 0f
            var sumT = 0f
            for (j in 0 until n){
                if (results[j].endDays<=i){
                    //добавляем последнее значение
                    results[j].duplicateLast()
                }
                sumC+=results[j].contacts[i].toFloat()
                sumL+=results[j].latent[i].toFloat()
                sumI+=results[j].infected[i].toFloat()
                sumIm+=results[j].immunity[i].toFloat()
                sumD+=results[j].died[i].toFloat()
                sumV+=results[j].vaccine[i].toFloat()
                sumH+=results[j].healthy[i].toFloat()
                sumT+=results[j].total[i].toFloat()
            }
            res.meanContacts.add(sumC/n.toFloat())
            res.meanLatent.add(sumL/n.toFloat())
            res.meanInfected.add(sumI/n.toFloat())
            res.meanImmunity.add(sumIm/n.toFloat())
            res.meanDied.add(sumD/n.toFloat())
            res.meanVaccine.add(sumV/n.toFloat())
            res.meanHealthy.add(sumH/n.toFloat())
            res.meanTotal.add(sumT/n.toFloat())
            if (res.isEnd()){
                res.lastDay = i
                break
            }
        }

        var sumH = 0f
        var sumI = 0f
        var sumD = 0f
        for (r in results){
            sumH+=r.endHealthy.toFloat()
            sumI+=r.endImmunity.toFloat()
            sumD+=r.endDied.toFloat()
        }
        res.endHealthy=sumH/n.toFloat()
        res.endImmunity=sumI/n.toFloat()
        res.endDied=sumD/n.toFloat()

        return res
    }
}