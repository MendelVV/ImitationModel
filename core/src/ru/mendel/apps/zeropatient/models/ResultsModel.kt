package ru.mendel.apps.zeropatient.models

class ResultsModel {

    var endHealthy = 0f
    var endImmunity = 0f
    var endDied = 0f

    val meanContacts = arrayListOf<Float>()
    val meanLatent = arrayListOf<Float>()
    val meanInfected = arrayListOf<Float>()
    val meanImmunity = arrayListOf<Float>()
    val meanDied = arrayListOf<Float>()
    val meanVaccine = arrayListOf<Float>()
    val meanHealthy = arrayListOf<Float>()
    val meanTotal = arrayListOf<Float>()

    var lastDay = 0

    init {
        meanContacts.add(0f)
        meanLatent.add(1f)
        meanInfected.add(0f)
        meanImmunity.add(0f)
        meanDied.add(0f)
        meanVaccine.add(0f)
        meanHealthy.add(0f)
        meanTotal.add(0f)
    }

    fun isEnd(): Boolean{
        return meanInfected.last()+meanLatent.last()<0.5f
    }
}