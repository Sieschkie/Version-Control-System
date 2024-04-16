package svcs

import java.io.File
import java.nio.file.Paths

val workDir = File(System.getProperty("user.dir"))
val vcsDir = File(workDir, "vcs")
val configFile = vcsDir.resolve("config.txt")
val indexFile =vcsDir.resolve("index.txt") //

fun help() {
    println("These are SVCS commands:\n" +
            "config     Get and set a username.\n" +
            "add        Add a file to the index.\n" +
            "log        Show commit logs.\n" +
            "commit     Save changes.\n" +
            "checkout   Restore a file.")
}
fun isValidInput(username: String): Boolean { //
    val regex = Regex("^[a-zA-Z][a-zA-Z0-9]*$")
    return username.isNotBlank() && username.length in 3..20 && regex.matches(username)
}
fun makeConfigAndIndexFiles(){
    if (!vcsDir.exists()) {
        try {
            vcsDir.mkdirs()
        } catch (e: Exception) {}
    }
    if (!configFile.exists()) {
        try {
            configFile.createNewFile()
        } catch (_: Exception) {}
    }
    if (!indexFile.exists()) {
        try {
            indexFile.createNewFile()
        } catch (_: Exception) {}
    }

}
fun config(name: String?) {
    if (name !== null){
        configFile.writeText(name)
    } else {
        val configValue = configFile.readText()
        if(configValue.isEmpty()) {
            println("Please, tell me who you are.")
            val username = readLine()?.trim() ?: ""
            if(isValidInput(username)) {
                configFile.writeText(username)
                return
            }
        }
        println("The username is $configValue")
    }
}
fun add(trackedFile: String?) {
    if(trackedFile !== null) {
        if (trackedFile.isNotBlank()) {
            val sourceFile = File(trackedFile)
            if (sourceFile.exists()) {
                    indexFile.appendText("$trackedFile \n")
                    println("File '$trackedFile' is tracked.") //вывод всех файлов отслеживаемых сделать
            } else {
                println("Can't find '$trackedFile'.")
            }
        }
    } else {
        val indexValue = indexFile.readLines()
        if(indexValue.isEmpty()) {
            println("Add a file to the index.")
            val track = readln()
            add(track)
        } else {
            println("Tracked files:")
            indexValue.forEach{println(it)}
        }
    }
}

fun main(args: Array<String>){
//fun main() { //test
    makeConfigAndIndexFiles()
    //val args = readln().split(" ") //test
    when(args.firstOrNull()?.lowercase()?.trim()) {
        null, "--help" -> help()
        "config" -> config(args.getOrNull(1)?.trim())
        "add" -> add(args.getOrNull(1)?.trim())
        "log" -> println("Show commit logs.")
        "commit" -> println("Save changes.")
        "checkout" -> println("Restore a file.")
        else -> println("'${args[0]}' is not a SVCS command.")
    }
}
