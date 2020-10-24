package ru.mendel.apps.zeropatient.models

class GameModel(n:Int, val target: Int) {

    companion object{
        const val MONEY = 10//дает один человек
        const val COST_HOSPITAL = 3000
        const val COST_QUARANTINE = 1000
        const val COST_VELOCITY = 5000
        const val COST_VACCINE = 3000
        const val MAX_VELOCITY = 2
    }

    //сколько каких
    var total = n
    var healthy = n
    var latent = 0
    var infected = 0
    var dead = 0
    var immunity = 0
    var vaccine = 0
    var totalInfections = 0

    var velocityBonus = 0

    var money = 0//денежки
    var days = 0

    fun addMoney(hospital: Int, quarantine: Int){
        money+= MONEY *(healthy+immunity+latent-hospital)+ MONEY *(infected-quarantine)/2
    }

    fun buyVelocity(){
        pay(getVelocityCost())
        velocityBonus++
    }

    fun getVelocityCost(): Int{
        return COST_VELOCITY +3000*velocityBonus
    }

    fun pay(cost: Int){
        money-=cost
    }

    fun addLatent(){
        healthy--
        latent++
        totalInfections++
    }

    fun addLatent(state: Int){
        when(state){
            PatientModel.STATE_HEALTHY -> healthy--
            PatientModel.STATE_IMMUNITY -> immunity--
            PatientModel.STATE_VACCINE -> vaccine--
        }
        latent++
        totalInfections++
    }

    fun addInfected(){
        latent--
        infected++
    }

    fun addDead(){
        infected--
        dead++
    }

    fun addImmunity(){
        infected--
        immunity++
    }

    fun addVaccine(){
        healthy--
        vaccine++
    }
}