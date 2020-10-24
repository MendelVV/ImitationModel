package ru.mendel.apps.zeropatient.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.scenes.scene2d.Actor
import ru.mendel.apps.zeropatient.ResourcesAll
import ru.mendel.apps.zeropatient.models.BoundsModel
import ru.mendel.apps.zeropatient.stages.MathStage

class FieldMathActor(img: String, val mathStage: MathStage) : Actor() {
    companion object{
        const val WIDTH = 900f
        const val HEIGHT = 900f
    }

    private val mTexture: Texture = ResourcesAll.getTexture(img)!!

    init {
        createBody()
    }

    private fun createBody(){
        //создаем границы
        //левая граница
        var def = BodyDef()
        def.type = BodyDef.BodyType.StaticBody //неподвижная граница

        var body: Body = mathStage.world.createBody(def)
        body.setTransform(0f, HEIGHT / 2, 0f) //это похоже центр

        var poly = PolygonShape()
        poly.setAsBox(1f, HEIGHT /2)
        var f = body.createFixture(poly, 1f)
        f.userData = BoundsModel(BoundsModel.TYPE_LEFT)
        poly.dispose()

        //правая граница

        //правая граница
        def = BodyDef()
        def.type = BodyDef.BodyType.StaticBody //неподвижная граница

        body = mathStage.world.createBody(def)
        body.setTransform(WIDTH, HEIGHT / 2, 0f) //это похоже центр

        poly = PolygonShape()
        poly.setAsBox(1f, HEIGHT /2)
        f = body.createFixture(poly, 1f)
        f.userData = BoundsModel(BoundsModel.TYPE_RIGHT)
        poly.dispose()

        //нижняя граница

        //нижняя граница
        def = BodyDef()
        def.type = BodyDef.BodyType.StaticBody //неподвижная граница

        body = mathStage.world.createBody(def)
        body.setTransform(WIDTH /2, 0f, 0f) //это похоже центр

        poly = PolygonShape()
        poly.setAsBox(WIDTH /2, 1f)
        f = body.createFixture(poly, 1f)
        f.userData = BoundsModel(BoundsModel.TYPE_BOTTOM)
        poly.dispose()

        //верхняя граница

        //верхняя граница
        def = BodyDef()
        def.type = BodyDef.BodyType.StaticBody //неподвижная граница

        body = mathStage.world.createBody(def)
        body.setTransform(WIDTH /2, HEIGHT, 0f) //это похоже центр

        poly = PolygonShape()
        poly.setAsBox(WIDTH /2, 1f)
        f = body.createFixture(poly, 1f)
        f.userData = BoundsModel(BoundsModel.TYPE_TOP)
        poly.dispose()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(mTexture,0f,0f, WIDTH, HEIGHT)
    }
}