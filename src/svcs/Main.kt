package svcs

import java.io.File

val workDir = File(System.getProperty("user.dir"))
val vcsDir = File(workDir, "vcs")
val commitsDir = File(workDir, "commits")
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
fun isValidInput(input: String): Boolean {
    val regex = Regex("^[a-zA-Z][a-zA-Z0-9_.]*$")
    return input.isNotBlank() && input.length in 3..20 && regex.matches(input)
}

fun makeConfigAndIndexFiles(){
    if (!vcsDir.exists()) {
            vcsDir.mkdirs()
    }
    if (!commitsDir.exists()) {
            vcsDir.mkdirs()
    }
    if (!configFile.exists()) {
            configFile.createNewFile()
    }
    if (!indexFile.exists()) {
            indexFile.createNewFile()
    }

}

fun config(name: String?) {
    if (name == null) {
        val configValue = configFile.readText()
        if(configValue.isEmpty()) {
            println("Please, tell me who you are.")
            val username = readLine()?.trim() ?: ""
            if(isValidInput(username)) {
                configFile.writeText(username)
                return
            } else { println("The '$name' contains prohibited characters.") }
        }
        println("The username is $configValue.")
    } else {
        if(isValidInput(name)) {
            configFile.writeText(name)
            println("The username is $name.")
        }
    }
}
fun add(trackedFile: String?) {
    if(trackedFile !== null) {
        if (trackedFile.isNotBlank()) {
            val sourceFile = File(trackedFile)
            if (sourceFile.exists()) {
                if(isValidInput(trackedFile)) {
                    indexFile.appendText("$trackedFile\n")
                    println("The File '$trackedFile' is tracked.")
                } else { println("The '$trackedFile' contains prohibited characters.") }
            } else {
                println("Can't find '$trackedFile'.")
            }
        }
    } else {
        val indexValue = indexFile.readLines()
        if(indexValue.isEmpty()) {
            println("Add a file to the index.")
            val track = readln()
            if(isValidInput(track)) {
                add(track)
            }
        } else {
            println("Tracked files:")
            indexValue.forEach{println(it)}
        }
    }
}

fun log() {
    println("Show commit logs.")
}

fun commit() {
    println("Save changes.")
}

fun main(args: Array<String>) {
//fun main() { //test
    makeConfigAndIndexFiles()
    //val args = readln().split(" ") //test
    when(args.firstOrNull()?.lowercase()?.trim()) {
        null, "--help" -> help()
        "config" -> config(args.getOrNull(1)?.trim())
        "add" -> add(args.getOrNull(1)?.trim())
        "log" -> log()
        "commit" -> commit()
        "checkout" -> println("Restore a file.")
        else -> println("'${args[0]}' is not a SVCS command.")
    }
}
