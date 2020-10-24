package ru.mendel.apps.zeropatient.models

class BoundsModel(val type: Int) {

    companion object{
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
        const val TYPE_TOP = 2
        const val TYPE_BOTTOM = 3
    }

}