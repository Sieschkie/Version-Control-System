package svcs

import java.io.File

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

fun config(name: String?) {
    val workingDirectory = System.getProperty("user.dir")
    val separator = File.separator
    val workDir = File(workingDirectory, "work_files")
    if (!workDir.exists()) {
        try {
            workDir.mkdirs()
        } catch (_: Exception) {}
    }

    val vcsDir = File(workDir, "vcs")
    if (!vcsDir.exists()) {
        try {
            vcsDir.mkdirs()
        } catch (e: Exception) {}
    }
    val configFile = vcsDir.resolve("config.txt")
    if (!configFile.exists()) {
        try {
            configFile.createNewFile()
        } catch (_: Exception) {}
    }
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

fun add(file: String?) {
    println("Add a file to the index.")
}
//fun main(args: Array<String>)
fun main() {
    val args = readln().split(" ") //test
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
