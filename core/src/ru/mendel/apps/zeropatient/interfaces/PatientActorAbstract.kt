package ru.mendel.apps.zeropatient.interfaces

import com.badlogic.gdx.scenes.scene2d.Actor
import ru.mendel.apps.zeropatient.geometry.Point

abstract class PatientActorAbstract: Actor() {
    var moveToCenter: Boolean = false
    abstract fun contact()
    abstract fun toPoint(point: Point, b: Boolean = true)
    abstract fun awayPoint(point: Point)
    abstract fun reverseVelocity(type: Int)
}