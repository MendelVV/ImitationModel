package ru.mendel.apps.zeropatient.models

import ru.mendel.apps.zeropatient.interfaces.PatientActorAbstract

class PatientModel {
    companion object{
        const val STATE_HEALTHY = 0
        const val STATE_LATENT = 1
        const val STATE_INFECTED = 2
        const val STATE_DEAD = 3
        const val STATE_IMMUNITY = 4
        const val STATE_VACCINE = 5
    }

    var state: Int = STATE_HEALTHY
    var actor: PatientActorAbstract? = null
    var inHospital: Boolean = false
    var inQuarantine: Boolean = false

    fun isInfectious(): Boolean{
//        return state == STATE_LATENT || state == STATE_INFECTED
        return state == STATE_INFECTED
    }

    fun isSusceptible():Boolean{
        return state==STATE_HEALTHY || state==STATE_IMMUNITY || state==STATE_VACCINE
    }
}