package ru.mendel.apps.zeropatient.models

class DiseaseModel {

    //модель болезни

    var infectiousness = 50//заразность из 100
    var latentPeriod = 10f//длина латентного периода
    var diseasePeriod = 30f//продолжительность болезни
    var mortality = 50//смертельность из 100
    var velocity = 40f
    var complexity = 30f//сложность болезни, через сколько откроют лекарство

    var hospitalLevel = 3//уровень госпиталя
    var size = 50//количество пациентов
    var vaccine = 0//количество вакцинированных

    var reinfection = 0f//на сколько умножить заразность если пациент уже переболел

}