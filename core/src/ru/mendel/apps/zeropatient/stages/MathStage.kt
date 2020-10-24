package ru.mendel.apps.zeropatient.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import ru.mendel.apps.zeropatient.actors.FieldMathActor
import ru.mendel.apps.zeropatient.GameHelper
import ru.mendel.apps.zeropatient.ZeroPatientGame
import ru.mendel.apps.zeropatient.actors.EndGameMathActor
import ru.mendel.apps.zeropatient.actors.PatientMathActor
import ru.mendel.apps.zeropatient.interfaces.SaveCsvInterface
import ru.mendel.apps.zeropatient.models.DiseaseModel
import ru.mendel.apps.zeropatient.models.GameModel
import ru.mendel.apps.zeropatient.models.MathModel
import ru.mendel.apps.zeropatient.models.PatientModel
import ru.mendel.apps.zeropatient.phisics.Collide
import ru.mendel.apps.zeropatient.phisics.CollideReInfection
import kotlin.random.Random

class MathStage: Stage {

    val game: ZeroPatientGame
    var pause = false
    val world = World(Vector2(0f, 0f), true)
    val diseaseModel: DiseaseModel
    lateinit var gameModel: GameModel
    private lateinit var mMultiplexer: InputMultiplexer
    private var mTime = 0f
    var mathModel = MathModel()
    val saveCsv: SaveCsvInterface

    constructor(gm: ZeroPatientGame, ds: DiseaseModel, sc: SaveCsvInterface):super(){
        game = gm
        this.diseaseModel = ds
        saveCsv = sc
        initialize()
    }

    constructor(gm: ZeroPatientGame, ds: DiseaseModel, sc: SaveCsvInterface, viewport: Viewport): super(viewport){
        game = gm
        this.diseaseModel = ds
        saveCsv = sc
        initialize()
    }

    constructor(gm: ZeroPatientGame, ds: DiseaseModel, sc: SaveCsvInterface, viewport: Viewport, batch: Batch): super(viewport, batch){
        game = gm
        this.diseaseModel = ds
        saveCsv = sc
        initialize()
    }
    private fun initialize() {

        mMultiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = mMultiplexer
        mMultiplexer.addProcessor(this@MathStage)

//        val collide = Collide(mathModel)
        val collide = CollideReInfection(mathModel)
        world.setContactFilter(collide)
        world.setContactListener(collide)

        gameModel = GameModel(diseaseModel.size, 0)

        val fieldActor = FieldMathActor("field_math.png",this)
        addActor(fieldActor)

        //добавляем всех актеров
//        addAllPatients()
        addAllPatientsVaccine()
    }

    private fun addAllPatients(){
        val points = GameHelper.generatePointsMath(gameModel.total)
        val pos = Random.nextInt(points.size)
        for ((i, point) in points.withIndex()){
            val patientModel = PatientModel()
            if (i==pos){
                patientModel.state=PatientModel.STATE_LATENT
                gameModel.addLatent()
            }
            val patientActor = PatientMathActor(point, patientModel, this@MathStage)
            addActor(patientActor)
        }
    }

    private fun addAllPatientsVaccine(){
        //добавляем всех пациентов но у некоторых уже есть иммунитет
        val points = GameHelper.generatePointsMath(gameModel.total)
        val pos = Random.nextInt(points.size)
        var n = diseaseModel.vaccine
        for ((i, point) in points.withIndex()){
            val patientModel = PatientModel()
            if (i==pos){
                patientModel.state=PatientModel.STATE_LATENT
                gameModel.addLatent()
            }else if (n>0){
                patientModel.state=PatientModel.STATE_VACCINE
                gameModel.addVaccine()
                n--
            }
            val patientActor = PatientMathActor(point, patientModel, this@MathStage)
            addActor(patientActor)
        }
    }

    private fun isEndGame():Boolean{
        return gameModel.infected+gameModel.latent==0
    }

    private fun endGame(){
        pause = true
        val endGameActor = EndGameMathActor()
        addActor(endGameActor)
        //сохраняем результат
        mathModel.endHealthy = gameModel.healthy
        mathModel.endImmunity = gameModel.immunity
        mathModel.endDied = gameModel.dead
        mathModel.endDays = gameModel.days
        saveCsv.addExperiment(mathModel)
        game.restartMathStage()
    }

    override fun act(delta: Float) {
        if (pause) return
        if (isEndGame()){
            //конец игры!
            endGame()
            return
        }
        super.act(delta)
        mTime+=delta
        if (mTime>1){
            mTime-=1
            gameModel.days++
            //обновляем модель
            mathModel.step(gameModel)
        }

        world.step(delta,8,3)
    }

    override fun keyUp(keyCode: Int): Boolean {
        if (keyCode== Input.Keys.SPACE){
            pause=!pause
            return true
        }
        return false
    }
}