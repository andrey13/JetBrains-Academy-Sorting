package sorting

import java.io.File
import java.util.*

fun readData(dataType: String): MutableList<String> {
    val data = mutableListOf<String>()
    val scanner = Scanner(System.`in`)
    while (scanner.hasNext()) {
        val s = scanner.nextLine().trim()
        when(dataType) {
            "long" -> data.addAll(s.split(Regex("\\s+")))
            "word" -> data.addAll(s.split(Regex("\\s+")))
            "line" -> data.add(s)
        }
    }
    return data
}

fun loadData(dataType: String, inputFile: String): MutableList<String> {
    val data = mutableListOf<String>()
    if (!File(inputFile).exists()) return data
    File(inputFile).forEachLine {
        when(dataType) {
            "long" -> data.addAll(it.split(Regex("\\s+")))
            "word" -> data.addAll(it.split(Regex("\\s+")))
            "line" -> data.add(it)
        }
    }
    return data
}

fun sortData(data: MutableList<String>, dataType: String): List<Any> {
    if (dataType == "long") return data.map { it -> it.toInt() }.sorted()
    return data.sorted()
}

fun rateData(data: MutableList<String>): List<List<String>> {
    val rateD = mutableListOf<MutableList<String>>()
    data.forEach { w ->
        var found = false
        rateD.forEach { it ->
            if (it[0] == w) {
                found = true
                it[1] = (it[1].toInt() + 1).toString()
            }
        }
        if (!found) rateD.add(mutableListOf(w, "1", "0"))
    }
    rateD.forEach { it -> it[2] = (it[1].toInt() * 100 / data.size).toString() }
    return rateD
}

fun sortRateData(data: List<List<String>>, dataType: String): List<List<String>> {
    if (dataType == "long") return data.sortedWith(compareBy( {it[2]}, {it[0].toInt()} ))
    return data.sortedWith(compareBy( {it[2]}, {it[0]} ))
}

fun parseArgs(args: Array<String>): List<String> {
    fun findOption(optionName: String, defaultOption: String): String {
        val indexOption = args.indexOf(optionName)
        return if (indexOption >= 0) {
            if  (indexOption != (args.size - 1)) {
                args[indexOption + 1]
            } else {
                "???"
            }
        } else {
            defaultOption
        }
    }

    val dataType = findOption("-dataType", "word")
    val sortType = findOption("-sortingType", "natural")
    val inputFile = findOption("-inputFile", "???")
    val outputFile = findOption("-outputFile", "???")

    return listOf(dataType, sortType, inputFile, outputFile)
}

fun main(args: Array<String>) {
    val (dataType, sortType, inputFile, outputFile) = parseArgs(args)

    fun myPrint(str: String) {
        if (outputFile == "???") {
            print(str)
        } else {
            val file = File(outputFile)
            file.appendText(str)
        }
    }


    if (dataType == "???") {
        println("No data type defined!")
        return
    }

    if (sortType == "???") {
        println("No sorting type defined!")
        return
    }

    val data = if (inputFile == "???") {
        readData(dataType)
    } else {
        loadData(dataType, inputFile)
    }

    myPrint("Total numbers: ${data.size}.\n")
    if (sortType == "natural") {
        myPrint("Sorted data: ")
        if (dataType == "line") {
            sortData(data, dataType).forEach { it -> myPrint("\n$it")}
        } else {
            sortData(data, dataType).forEach { it -> myPrint("$it ")}
        }
    } else {
        sortRateData(rateData(data), dataType).forEach { it -> myPrint("${it[0]}: ${it[1]} time(s), ${it[2]}%\n") }
    }
}

