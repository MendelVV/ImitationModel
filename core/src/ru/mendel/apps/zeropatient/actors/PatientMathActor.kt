package ru.mendel.apps.zeropatient.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import ru.mendel.apps.zeropatient.BasicInputProcessor
import ru.mendel.apps.zeropatient.GameHelper
import ru.mendel.apps.zeropatient.ResourcesAll
import ru.mendel.apps.zeropatient.ZeroPatientGame
import ru.mendel.apps.zeropatient.geometry.Point
import ru.mendel.apps.zeropatient.interfaces.PatientActorAbstract
import ru.mendel.apps.zeropatient.models.PatientModel
import ru.mendel.apps.zeropatient.stages.MathStage
import kotlin.random.Random

class PatientMathActor(point: Point, val patient: PatientModel, val mathStage: MathStage) : PatientActorAbstract(),
        BasicInputProcessor {

    companion object{
        const val SIZE = 20f
        const val DELTA = 5.0f

        const val REVERSE_BY_X = 0
        const val REVERSE_BY_Y = 1
    }

    private lateinit var mBody: Body
    private var texture: TextureRegion? = null
//    private val textureHealth = ResourcesAll.getTextureRegion("p_health", ResourcesAll.ATLAS_ICONS)
//    private val textureStop = ResourcesAll.getTextureRegion("p_stop", ResourcesAll.ATLAS_ICONS)
    private var mDelta = 5f
    private var mLatent = 0f
    private var mPeriod = 0f
    private var mDeadPeriod = 3f
    private val diseaseModel = mathStage.diseaseModel

    init {
        patient.actor = this
        createBody(point.x, point.y)
        setTexture()

    }

    private fun createBody(x: Float, y: Float){
        val world = mathStage.world
        val def = BodyDef()
        def.type = BodyDef.BodyType.DynamicBody
        mBody = world.createBody(def)
        mBody.setTransform(x, y, 0f)

        //создаем тела для бонусов которые должны быть вокруг

        val circleShape = CircleShape()
        circleShape.radius = SIZE /2
        val fixture = mBody.createFixture(circleShape, 1f)
        fixture.userData = patient
        circleShape.dispose()

    }

    private fun setTexture(){

        val img = when(patient.state){
            PatientModel.STATE_HEALTHY -> "patient_healthy"
            PatientModel.STATE_LATENT -> "patient_latent"
            PatientModel.STATE_INFECTED -> "patient_infected"
            PatientModel.STATE_DEAD -> "patient_dead"
            PatientModel.STATE_IMMUNITY -> "patient_immunity"
            PatientModel.STATE_VACCINE -> "patient_vaccine"
            else -> ""
        }
        texture = ResourcesAll.getTextureRegion(img, ResourcesAll.ATLAS_PATIENT)
    }

    private fun setRandomVelocity(){
        var dx = 10-Random.nextInt(21)
        if (dx==0) dx++
        var dy = 10-Random.nextInt(21)
        if (dy==0) dy++
        val vector2 = Vector2(dx.toFloat(), dy.toFloat())
        vector2.setLength(diseaseModel.velocity)
        mBody.linearVelocity = vector2
    }

    override fun reverseVelocity(type: Int){
        //либо по x либо по y
        if (type== REVERSE_BY_X){
            val velocity = mBody.linearVelocity
            velocity.x = -velocity.x
            mBody.linearVelocity = velocity
        }else{
            val velocity = mBody.linearVelocity
            velocity.y = -velocity.y
            mBody.linearVelocity = velocity
        }
    }

    override fun toPoint(point: Point, b: Boolean){
        moveToCenter = b
        val velocity = Vector2(point.x-mBody.position.x, point.y-mBody.position.y)
        velocity.setLength(diseaseModel.velocity)
        mDelta = 0f
        mBody.linearVelocity = velocity
    }

    override fun awayPoint(point: Point){
        val velocity = Vector2(mBody.position.x-point.x, mBody.position.y-point.y)
        velocity.setLength(diseaseModel.velocity)
        mDelta = 0f
        mBody.linearVelocity = velocity
    }

    override fun contact(){
        //возможно заражение
        var infectiousness = diseaseModel.infectiousness.toFloat()
        if (patient.state==PatientModel.STATE_IMMUNITY || patient.state==PatientModel.STATE_VACCINE){
            infectiousness*=diseaseModel.reinfection
        }
        val p = Random.nextInt(100)

        if (p<infectiousness){
            //заразился
            mathStage.gameModel.addLatent(patient.state)
            patient.state = PatientModel.STATE_LATENT
            mLatent = 0f
            mPeriod = 0f
            setTexture()
        }
    }

    fun getCenter(): Point{
        return Point(mBody.position.x, mBody.position.y)
    }

    override fun act(delta: Float) {
        super.act(delta)

        //переодически будет меняться направление движения
        if (patient.state==PatientModel.STATE_DEAD){
            mDeadPeriod-=delta
            if (mDeadPeriod<=0){
                //убираем чувака
                mBody.isActive = false
                mathStage.world.destroyBody(mBody)
                remove()
            }
            return
        }
        mDelta+=delta
        if (0.5f<mDelta && moveToCenter){
            moveToCenter = false
        }
        if (DELTA <mDelta){
            setRandomVelocity()
            mDelta=0f-3*Random.nextFloat()
        }
        if (patient.state==PatientModel.STATE_LATENT){
            mLatent+=delta
            if (mLatent>diseaseModel.latentPeriod){
                mathStage.gameModel.addInfected()
                patient.state = PatientModel.STATE_INFECTED
                setTexture()
            }
        }
        if (patient.state==PatientModel.STATE_INFECTED){
            mPeriod+=delta
            if (patient.inHospital){
                mPeriod+=2*delta
            }
            if (mPeriod>diseaseModel.diseasePeriod){
                endDisease()
                setTexture()
            }
        }
    }

    private fun endDisease(){
        val isDead = GameHelper.isDead(diseaseModel, patient.inHospital)
        if (isDead){
            mathStage.gameModel.addDead()
            patient.state = PatientModel.STATE_DEAD
            val velocity = Vector2(0f,0f)
            mBody.linearVelocity=velocity
         }else{
            mathStage.gameModel.addImmunity()
            patient.state = PatientModel.STATE_IMMUNITY
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.draw(texture,mBody.position.x - SIZE /2, mBody.position.y - SIZE /2, SIZE, SIZE)
    }
}