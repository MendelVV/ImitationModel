package ru.mendel.apps.zeropatient.phisics

import com.badlogic.gdx.physics.box2d.*
import ru.mendel.apps.zeropatient.actors.PatientMathActor
import ru.mendel.apps.zeropatient.models.BoundsModel
import ru.mendel.apps.zeropatient.models.MathModel
import ru.mendel.apps.zeropatient.models.PatientModel


class CollideReInfection(val mathModel: MathModel? = null) : ContactFilter, ContactListener {

    companion object{
        const val GROUP_PATIENT = 0
        const val GROUP_BOUNDS = 1
        const val GROUP_HOSPITAL = 2
        const val GROUP_QUARANTINE = 3
        const val GROUP_HALO = 4
    }

    override fun shouldCollide(fixtureA: Fixture?, fixtureB: Fixture?): Boolean {
        //обрабатывать или нет коллизию?
//        fixtureA!!.userData
//        fixtureB!!.userData
        //пока что во всех случаях обрабатываем
        return true
    }

    override fun endContact(contact: Contact?) {

    }

    override fun beginContact(contact: Contact?) {
        var model1 = contact!!.fixtureA.userData
        var model2 = contact.fixtureB.userData

        var type1 = getGroup(model1)
        var type2 = getGroup(model2)

        if (type1>type2){
            val type = type1
            val model = model1
            type1 = type2
            model1=model2
            type2=type
            model2=model
        }

        if(type1== GROUP_PATIENT && type2== GROUP_PATIENT){
            //обрабатываем столкновение двух пациентов
            val p1 = model1 as PatientModel
            val p2 = model2 as PatientModel
            if (mathModel!=null){
                //считаем каждый социальный контакт
                mathModel.allContacts++
            }
            if (p1.isInfectious() && p2.isSusceptible()){
                p2.actor!!.contact()
            }else if(p2.isInfectious() && p1.isSusceptible()){
                p1.actor!!.contact()
            }
        }else if(type1== GROUP_PATIENT && type2== GROUP_BOUNDS){
            boundCollide(model1 as PatientModel, (model2 as BoundsModel).type)
        }

    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
        try{
            val model1 = contact!!.fixtureA.userData
            val model2 = contact.fixtureB.userData
            var type1 = getGroup(model1)
            var type2 = getGroup(model2)

            if (type1>type2){
                val type = type1
                type1 = type2
                type2=type
            }

            contact.isEnabled = (type1== GROUP_PATIENT && type2== GROUP_BOUNDS)
            if (type1== GROUP_PATIENT && type2== GROUP_HOSPITAL){
                contact.isEnabled = false
            }else if (type1== GROUP_PATIENT && type2== GROUP_QUARANTINE){
                contact.isEnabled = false
            }else if(type1== GROUP_PATIENT && type2== GROUP_HALO){
                contact.isEnabled = false
            }
            //работает ли столкновение физически, возможно нужно будет добавить для столкновения двух пациентов
        }catch (e: Exception){
            contact!!.isEnabled = false
        }
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }

    private fun getGroup(model: Any): Int{
        when (model) {
            is PatientModel -> return GROUP_PATIENT
            is BoundsModel -> return GROUP_BOUNDS
        }
        return -1
    }

    private fun boundCollide(patient: PatientModel, type: Int){
        if (type==BoundsModel.TYPE_TOP || type==BoundsModel.TYPE_BOTTOM){
            patient.actor!!.reverseVelocity(PatientMathActor.REVERSE_BY_Y)
        }else{
            patient.actor!!.reverseVelocity(PatientMathActor.REVERSE_BY_X)
        }
    }

}