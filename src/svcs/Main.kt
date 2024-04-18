package svcs

import java.io.File
import java.util.*
import java.security.MessageDigest

// Define directory and file paths
val workDir = File(System.getProperty("user.dir"))
val vcsDir = File(workDir, "vcs")
val commitsDir = File(vcsDir, "commits")
val configFile = vcsDir.resolve("config.txt")
val indexFile = vcsDir.resolve("index.txt")
val logFile = vcsDir.resolve("log.txt")

// Function to display available SVCS commands
fun help() {
    println("These are SVCS commands:\n" +
            "config     Get and set a username.\n" +
            "add        Add a file to the index.\n" +
            "log        Show commit logs.\n" +
            "commit     Save changes.\n" +
            "checkout   Restore a file.")
}

fun isValidInput(input: String): Boolean {
    // Regular expression to match valid input
    val regex = Regex("^[a-zA-Z][a-zA-Z0-9_.]*$")
    return input.isNotBlank() && input.length in 3..40 && regex.matches(input)
}

// Check and create necessary directories and files
fun makeDirAndFiles(){
    if (!vcsDir.exists()) vcsDir.mkdirs()
    if (!commitsDir.exists()) commitsDir.mkdir()
    if (!configFile.exists()) configFile.createNewFile()
    if (!indexFile.exists()) indexFile.createNewFile()
    if (!logFile.exists()) logFile.createNewFile()
}

// Function to handle 'config' command
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

// Function to handle 'add' command
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

// Function to display commit logs
fun log() {
    if(logFile.length() == 0L) {                //is log.txt empty?
        println("No commits yet.") }
    else {
        logFile.forEachLine { println(it) }     //if not, print it
    }
}

// Checks if there are changes in the tracked files by comparing them with the files in the last commit.
fun checkChanges(trackedFiles: List<String>, lastCommitDir: File): Boolean {
    var changed = false
    val md = MessageDigest.getInstance("SHA-256")

    trackedFiles.forEach { fileName ->
        val originalFile = File(System.getProperty("user.dir"), fileName)
        val commitFile = File(lastCommitDir, fileName)

        // Check if the commit file exists in the commits directory
        if (commitFile.exists()) {
            // If the hashes of the original and commit files don't match, set 'changed' to true
            if (!compareHashes(originalFile, commitFile, md)) {
                changed = true
                return@forEach
            }
        } else println("Unable to checkout to this commit because some tracked files are missing in it.")
    }
    return changed
}

// Function to compare hashes of two files (Algorithm SHA-256)
fun compareHashes(file1: File, file2: File, md: MessageDigest): Boolean {
    // Compute hash for the first file
    val hash1 = md.digest(file1.readBytes())

    // Compute hash for the second file
    val hash2 = md.digest(file2.readBytes())

    // Compare the hashes
    return hash1.contentEquals(hash2)
}

// Function to handle 'commit' command
fun commit(commit : String?) {
    if (commit == null || commit.isBlank()) {
        println("Message was not passed.")
        return
    } else {
        val trackedFiles = indexFile.readLines()
        val commitID = UUID.randomUUID().toString()
        val lastCommitID = logFile.readLines().firstOrNull()?.substringAfter("commit ")
        val commitDir = if (lastCommitID != null && commitsDir.listFiles()?.isNotEmpty() == true) {
            val lastCommitDir = File(commitsDir, lastCommitID)
            val changed = checkChanges(trackedFiles, lastCommitDir)
            if (changed) {
                File(commitsDir, commitID).apply { mkdir() }
            } else {
                println("Nothing to commit.")
                return
            }
        } else {
            File(commitsDir, commitID).apply { mkdir() }
        }
        trackedFiles.forEach { fileName ->
            val originalFile = File(workDir, fileName)
            val commitFile = File(commitDir, fileName)
            originalFile.copyTo(commitFile, overwrite = true)
        }
        writeLogs(commitID, commit)
        println("Changes are committed.")
    }
}

// Function to write commit logs
fun writeLogs(id: String, message: String) {
    val author= configFile.readText()
    val log = logFile.readText()                             // Save commits
    logFile.writeText("commit $id\n")                   // last commit on top
    logFile.appendText("Author: $author\n")
    logFile.appendText("${message.filter{it !='"'}}\n")
    logFile.appendText("\n$log")                        // old commits add
}

//
fun checkout(commitID: String?) {
    if (commitID == null) {
        println("Commit id was not passed.")
    } else {
        val lastCommitDir = File(commitsDir, commitID)
        if (!lastCommitDir.exists()) {
            println("Commit does not exist.")
        } else {                                            // If a commit with the given ID exists
            val trackedFiles = indexFile.readLines()        // the contents of the tracked file should be restored
            trackedFiles.forEach { fileName ->              // in accordance with this commit.
                val originalFile = File(workDir, fileName)
                val commitFile = File(lastCommitDir, fileName)
                commitFile.copyTo(originalFile, overwrite = true)
            }
            println("Switched to commit $commitID.")
        }
    }
}

fun main(args: Array<String>) {
    makeDirAndFiles()
    val arg = args.getOrNull(1)?.trim()
    when(args.firstOrNull()?.lowercase()?.trim()) {
        null, "--help" -> help()
        "config" -> config(arg)
        "add" -> add(arg)
        "log" -> log()
        "commit" -> commit(arg)
        "checkout" -> checkout(arg)
        else -> println("'${args[0]}' is not a SVCS command.")
    }
}