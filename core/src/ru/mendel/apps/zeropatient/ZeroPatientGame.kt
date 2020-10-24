package ru.mendel.apps.zeropatient

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import ru.mendel.apps.zeropatient.interfaces.SaveCsvInterface
import ru.mendel.apps.zeropatient.models.DiseaseModel
import ru.mendel.apps.zeropatient.stages.MathStage


class ZeroPatientGame(val saveCsv: SaveCsvInterface?) : ApplicationAdapter() {

    companion object {

        const val WIDTH_MATH = 900f
        const val HEIGHT_MATH = 900f
    }

    var mStage: Stage? = null
    var experimentNumber = 0

    override fun create() {
        super.create()
        ResourcesAll.initialize()
        ResourcesAll.path = saveCsv!!.getPath()
        setMathStage()
    }

    private fun setMathStage(){
        restartMathStage()
    }

    fun restartMathStage(){

        val experiment = ResourcesAll.getExperiment()
        val diseaseModel = experiment.disease
//        val diseaseModel = DiseaseModel()
        if (experimentNumber==experiment.count){
            saveCsv!!.saveData(experiment.path, diseaseModel)
//            saveCsv!!.saveData("e://", diseaseModel)
            return
        }
        experimentNumber++
        println("experiment num=${experimentNumber}")
        //
        mStage = MathStage(this, diseaseModel, saveCsv!!, StretchViewport(WIDTH_MATH, HEIGHT_MATH))
    }


    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        mStage?.act()
        mStage?.draw()
    }

    override fun dispose() {
        mStage?.dispose()
    }

}