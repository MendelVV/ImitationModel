package ru.mendel.apps.zeropatient

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.google.gson.Gson
import ru.mendel.apps.zeropatient.models.DiseaseModel
import ru.mendel.apps.zeropatient.models.ExperimentModel
import java.io.File


object ResourcesAll {

    const val ATLAS_PATIENT = 0
    const val ATLAS_ICONS = 1
    const val ATLAS_BUILDS = 2

    private var mTextureMap: MutableMap<String, Texture> = mutableMapOf()
    private var mAtlasMap: MutableMap<Int, TextureAtlas> = mutableMapOf()
    var path = ""

    fun initialize(){
        val list: MutableList<String> = mutableListOf()
        list.add("field_math.png")
        list.add("black.png")

        for (str in list) {
            val texture = Texture("textures/$str")
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            mTextureMap[str] = texture
        }

        list.clear()
        list.add("patient.atlas")
        list.add("icons.atlas")
        list.add("builds.atlas")

        for ((i, str) in list.withIndex()) {
            val atlas = TextureAtlas(Gdx.files.internal("atlases/$str"))
            mAtlasMap[i] = atlas
        }

    }

    fun getExperiment(): ExperimentModel {
        val fileName: String = "experiment.json"

        val data = File(path+"\\"+fileName).readText()
        println(data)
//        val data = Gdx.files.internal("levels/$fileName").readString()
        val experiment = Gson().fromJson(data,ExperimentModel::class.java)
/*        val experiment = ExperimentModel(1, "")
        val disease = DiseaseModel()
        disease.size = 300
        disease.vaccine = 0
        disease.velocity = 50f
        experiment.disease = disease
*/
        return experiment
    }

    fun getTexture(str: String): Texture? {
        return if (mTextureMap.containsKey(str)) {
            mTextureMap[str]
        } else null
    }

    fun getTextureRegion(str: String): TextureRegion? {
        return if (mTextureMap.containsKey(str)) {
            TextureRegion(mTextureMap[str])
        } else null
    }

    fun getTextureRegion(str: String, atlas: Int): TextureRegion? {
        return if (mAtlasMap.containsKey(atlas)){
            mAtlasMap[atlas]!!.findRegion(str)
        }else{
            null
        }
    }

}