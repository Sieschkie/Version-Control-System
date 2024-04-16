package svcs

import java.io.File
import java.nio.file.Paths
import kotlin.io.path.listDirectoryEntries

val workingDirectory = System.getProperty("user.dir")
val workDir = File(workingDirectory, "work_files")
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
    if (!workDir.exists()) {
        try {
            workDir.mkdirs()
        } catch (_: Exception) {}
    }
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
                val destinationPath = Paths.get(workDir.toString(), trackedFile)
                val destinationFile = destinationPath.toFile()
                try {
                    sourceFile.copyTo(destinationFile, true)
                    indexFile.appendText(trackedFile)
                    println("File '$trackedFile' is tracked.") //вывод всех файлов отслеживаемых сделать
                } catch (e: Exception) {}
            } else {
                println("Can't find '$trackedFile'.")
            }
        } else {
            println("Please enter a valid file name.")
        }
    } else {
        val indexValue = indexFile.readText()
        if(indexValue.isEmpty()) {
            println("Add a file to the index.")
            val track = readln().toString()
            add(track)
        } else {
            println("Tracked files:")
            indexValue.forEach { println(it) }
        }
    }
}
/*fun addTrackedFile(trackedFile: String) {
    if (trackedFile.isNotBlank()) {
        val sourceFile = File(trackedFile)
        if (sourceFile.exists()) {
            val destinationPath = Paths.get(workDir.toString(), trackedFile)
            val destinationFile = destinationPath.toFile()
            try {
                sourceFile.copyTo(destinationFile, true)
                println("File '${destinationPath.listDirectoryEntries()}' is tracked.") //вывод всех файлов отслеживаемых сделать
            } catch (e: Exception) {}
        } else {
            println("Can't find '$trackedFile'.")
        }
    } else {
        println("Please enter a valid file name.")
    }
}

fun add(file: String?) {
    if(file == null) {
        val files = workDir.walkTopDown()
                .filter { it.isFile }
                .toList()
        if(files.isNotEmpty()) {
            println("Tracked files:") //if files existed
            files.forEach { println(it) }
        } else {
            println("Add a file to the index.") // add fun
            val trackedFile = readln().toString()
            addTrackedFile(trackedFile)
            }
    } else { //если имя файла передали
        if(!File(file).exists()) {
            println("Can't find '$file'.")
        } else {
            addTrackedFile(file)
        }
    }
}
*/
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
