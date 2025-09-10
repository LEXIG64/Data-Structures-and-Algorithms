//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")

    for (i in 1..5) {
        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
        println("i = $i")
    }
}


val digits: String = "0123456789"
val lowercase: String = "abcdefghijklmnopqrstuvwxyz"
val uppercase: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
val punctuation: String = ".?!"

fun notCommon(password: String): Boolean {
    if (password == "123456" ||
        password == "qwerty" ||
        password == "password") {
        return false
    }
    return true
}

fun lengthRestriction(password: String): Boolean {
    if (password.length in 6..16) {
        return true
    }
    return false
}

fun occurrenceCount(target: String, charactersTocount: String): Int {
   var occurrences: Int = 0

    for (character in target)
        if (charactersTocount.contains(character)) {
            occurrences += 1
        }
    return occurrences

}

fun useAllClasses(password: String): Boolean {
    if (
        (occurrenceCount(password, digits) >= 1) &&
        (occurrenceCount(password, lowercase) >= 1) &&
        (occurrenceCount(password, uppercase) >= 1) &&
        (occurrenceCount(password, punctuation) >= 1)
        ) {

        return true
    }
    return false
}

fun allRules(password: String): Boolean {
    if (password.length > 16)
        return true
    if (
        (notCommon(password)) &&
        (lengthRestriction(password)) &&
        (useAllClasses(password))
    )
    {

        return true
    }
    return false
}

