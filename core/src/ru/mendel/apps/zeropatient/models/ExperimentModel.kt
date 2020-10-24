package ru.mendel.apps.zeropatient.models

class ExperimentModel(var count:Int = 0, var path:String = "") {
    var disease = DiseaseModel()
}