

fun tableDisplay (dataTable: Array<Array<Any>>) {
    val x = "Dimension"
    val y = "# of Points"
    val z = "Time"
    var table = "%15s | %15s | %15s\n".format(x, y, z)
    table += "-".repeat(16) + "|" + "-".repeat(17) + "|" + "-".repeat(16) + "\n"
    for (row in 1 .. 7) { // row is the dimensionality
        for (col in 1 .. 5) { // column is the number of points in the tree
            table += "%15s | %15s | %15s\n".format(
                dataTable[row][0].toString(),
                dataTable[0][col].toString(),
                dataTable[row][col].toString())
            table += "-".repeat(16) + "|" + "-".repeat(17) + "|" + "-".repeat(16) + "\n"
        }
    }

    print(table)
}

fun main() {
    println("The different runtimes to construct the KD Trees")
    org.example.tableDisplay(buildDataTable)

    println("The different runtimes to perform nearest neighbor search using KD Trees")
    org.example.tableDisplay(kdSearchDataTable)

    println("The different runtimes to perform nearest neighbor search using Brute Force")
    org.example.tableDisplay(bruteSearchDataTable)
}