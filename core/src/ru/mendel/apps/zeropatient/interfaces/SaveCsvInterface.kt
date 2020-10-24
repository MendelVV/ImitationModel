package ru.mendel.apps.zeropatient.interfaces

import ru.mendel.apps.zeropatient.models.DiseaseModel
import ru.mendel.apps.zeropatient.models.GameModel
import ru.mendel.apps.zeropatient.models.MathModel

interface SaveCsvInterface {

    fun getPath(): String
    fun addExperiment(mathModel: MathModel)
    fun saveData(filePath: String, diseaseModel: DiseaseModel)

}