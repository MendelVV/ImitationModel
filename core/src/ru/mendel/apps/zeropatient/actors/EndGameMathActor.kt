package ru.mendel.apps.zeropatient.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import ru.mendel.apps.zeropatient.ResourcesAll
import ru.mendel.apps.zeropatient.ZeroPatientGame

class EndGameMathActor() : Actor() {

    private val background = ResourcesAll.getTexture("black.png")

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.draw(background, 0f,0f, ZeroPatientGame.WIDTH_MATH, ZeroPatientGame.HEIGHT_MATH)
    }
}