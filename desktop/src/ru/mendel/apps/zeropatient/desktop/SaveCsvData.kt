package ru.mendel.apps.zeropatient.desktop

import ru.mendel.apps.zeropatient.ExperimentHelper
import ru.mendel.apps.zeropatient.interfaces.SaveCsvInterface
import ru.mendel.apps.zeropatient.models.DiseaseModel
import ru.mendel.apps.zeropatient.models.MathModel
import java.io.FileWriter
import java.nio.file.Paths

class SaveCsvData() : SaveCsvInterface {

    val results = arrayListOf<MathModel>()


    override fun getPath(): String {
        val currentRelativePath = Paths.get("")
        val path = currentRelativePath.toAbsolutePath().toString()
        return path
    }

    override fun addExperiment(mathModel: MathModel){
        results.add(mathModel)
    }

    override fun saveData(filePath: String, diseaseModel: DiseaseModel){
        //сохраняем все что есть
        val headerDisease = "size;velocity;infectiousness;mortality;latentPeriod;diseasePeriod;vaccine;reinfection"//заголовок данных по болезни
        val lineDisease = "${diseaseModel.size};${diseaseModel.velocity.toInt()};${diseaseModel.infectiousness};${diseaseModel.mortality};" +
                "${diseaseModel.latentPeriod.toInt()};${diseaseModel.diseasePeriod.toInt()};${diseaseModel.vaccine};${diseaseModel.reinfection}"

//================================================================================
        ExperimentHelper.sortAndFill(results)

        val list = arrayListOf("latent", "infected","immunity","died","vaccine","healthy","total")
        for ((i, s) in list.withIndex()){
            val fileNameArr = "$filePath$s.csv"
            writeArray(fileNameArr, i)
        }

//=================================================================================


        val fileNameEnd = filePath+"disease_end.csv"
        val fileWriterEnd = FileWriter(fileNameEnd)

        fileWriterEnd.append(headerDisease+"\n")
        fileWriterEnd.append(lineDisease+"\n")

        val headerEnd = "immunity;healthy;dead;days"
        fileWriterEnd.append(headerEnd+"\n")

//        ExperimentHelper.sortAndRemove(results)
        val res = ExperimentHelper.createMeans(results)

        var line = "${res.endImmunity};${res.endHealthy};${res.endDied};${res.lastDay}"
        fileWriterEnd.append(line.replace(".",","))

        fileWriterEnd.close()


        val fileNameCourse = filePath+"disease_course.csv"
        val fileWriterCourse = FileWriter(fileNameCourse)
        fileWriterCourse.append(headerDisease+"\n")
        fileWriterCourse.append(lineDisease+"\n")

        line="Contacts;${res.meanContacts.joinToString (separator = ";")}"
        fileWriterCourse.append("${line.replace(".",",")}\n")

        line="Latent;${res.meanLatent.joinToString (separator = ";")}"
        fileWriterCourse.append("${line.replace(".",",")}\n")

        line="Infected;${res.meanInfected.joinToString (separator = ";")}"
        fileWriterCourse.append("${line.replace(".",",")}\n")

        line="Immunity;${res.meanImmunity.joinToString (separator = ";")}"
        fileWriterCourse.append("${line.replace(".",",")}\n")

        line="Died;${res.meanDied.joinToString (separator = ";")}"
        fileWriterCourse.append("${line.replace(".",",")}\n")

        line="Vaccine;${res.meanVaccine.joinToString (separator = ";")}"
        fileWriterCourse.append("${line.replace(".",",")}\n")

        line="Healthy;${res.meanHealthy.joinToString (separator = ";")}"
        fileWriterCourse.append("${line.replace(".",",")}\n")

        line="Total;${res.meanTotal.joinToString (separator = ";")}"
        fileWriterCourse.append(line.replace(".",","))

        fileWriterCourse.close()
    }

    private fun writeArray(fileName: String, type: Int){
        val end = results.last().endDays
        //таблица в которой выводится общее число случаев заражения отдельно по каждому эксперименту

        val fileWriter = FileWriter(fileName)
        val days = IntRange(0,end-1).map { "day_$it" }
        val header =days.joinToString (separator = ";" )
        fileWriter.append("num;$header\n")
        for ((i, row) in results.withIndex()){
            val line= row.getArray(type).joinToString (separator = ";")
            fileWriter.append("e_$i;${line.replace(".",",")}\n")
        }
        fileWriter.close()
    }

}