package ru.mendel.apps.zeropatient.interfaces

import ru.mendel.apps.zeropatient.geometry.Point


interface HaloInterface {

    fun getCenter(): Point

    fun isFull():Boolean
}