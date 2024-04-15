package svcs

fun help() {
    println("These are SVCS commands:\n" +
            "config     Get and set a username.\n" +
            "add        Add a file to the index.\n" +
            "log        Show commit logs.\n" +
            "commit     Save changes.\n" +
            "checkout   Restore a file.")
}

fun main(args: Array<String>) {
    when(args.firstOrNull()?.lowercase()?.trim()) {
        null, "--help" -> help()
        "config" -> println("Get and set a username.")
        "add" -> println("Add a file to the index.")
        "log" -> println("Show commit logs.")
        "commit" -> println("Save changes.")
        "checkout" -> println("Restore a file.")
        else -> println("'${args[0]}' is not a SVCS command.")
    }
}
