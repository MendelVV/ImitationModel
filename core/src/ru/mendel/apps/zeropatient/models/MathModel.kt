package ru.mendel.apps.zeropatient.models

class MathModel {

    companion object{
        const val LATENT = 0
        const val INFECTED = 1
        const val IMMUNITY = 2
        const val DIED = 3
        const val VACCINE = 4
        const val HEALTHY = 5
        const val TOTAL = 6

    }

    var allContacts: Int = 0

    var endHealthy = 0
    var endImmunity = 0
    var endDied = 0
    var endDays = 0

    var contacts = arrayListOf<Int>()
    var latent = arrayListOf<Int>()
    var infected = arrayListOf<Int>()
    var immunity = arrayListOf<Int>()
    var died = arrayListOf<Int>()
    var vaccine = arrayListOf<Int>()
    var healthy = arrayListOf<Int>()
    var total = arrayListOf<Int>()

    fun step(gameModel: GameModel){
        //обновляем модель
        contacts.add(allContacts)
        latent.add(gameModel.latent)
        infected.add(gameModel.infected)
        immunity.add(gameModel.immunity)
        died.add(gameModel.dead)
        vaccine.add(gameModel.vaccine)
        healthy.add(gameModel.healthy+gameModel.vaccine)
        total.add(gameModel.totalInfections)
    }

    fun getArray(type: Int):ArrayList<Int>{
        return when (type){
            LATENT -> latent
            INFECTED -> infected
            IMMUNITY -> immunity
            DIED -> died
            VACCINE -> vaccine
            HEALTHY -> healthy
            TOTAL -> total
            else -> arrayListOf()
        }
    }

    fun duplicateLast(){
        endDays++
        contacts.add(contacts.last())
        latent.add(0)
        infected.add(0)
        immunity.add(immunity.last())
        died.add(died.last())
        healthy.add(healthy.last())
        vaccine.add(vaccine.last())
        total.add(total.last())
    }
}