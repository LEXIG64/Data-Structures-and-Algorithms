package org.example

import java.io.File
import kotlin.text.iterator

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val wordFrequency = File("unigram_freq.csv")
    val frequencyDict = mutableListOf<Pair<String, Long>>()
    wordFrequency.forEachLine { line ->
        val column = line.split(",")
        frequencyDict.add(Pair(column[0], column[1].toLong()))
    }

    val dict = File("words_alpha.txt")
    val correctDictionary = mutableSetOf<String>()
    dict.forEachLine { line ->
        correctDictionary.add(line)
    }

    val english = mutableListOf<String>()
    val weightedWords = mutableListOf<Pair<String, Long>>()

    for (d in frequencyDict) {
        if (d.first in correctDictionary) {
            weightedWords.add(d)
            english.add(d.first)
        }
    }

    val englishTrie = Trie(weightedWords, english)
    val englishTrieRoot = englishTrie.startRoot
    val englishMap = englishTrie.makeMap()

    englishMap["rich"]?.childLetters
    englishTrieRoot.childLetters

    englishTrie.insert("lexi", 1)
    englishTrie.search("lexi")
    englishTrie.pop("lexi")

    println(spellCheck("apolofy", 5, englishTrie))
    println(predictText("th", englishTrie))
}

val alphabetWeights: MutableMap<String, Long> = mutableMapOf(
    "a" to 8167, "b" to 1492, "c" to 2782, "d" to 4253, "e" to 12702,
    "f" to 2228, "g" to 2015, "h" to 6094, "i" to 6966, "j" to 153,
    "k" to 772, "l" to 4025, "m" to 2406, "n" to 6749, "o" to 7507,
    "p" to 1929, "q" to 95, "r" to 5987, "s" to 6327, "t" to 9056,
    "u" to 2758, "v" to 978, "w" to 2360, "x" to 150, "y" to 1974,
    "z" to 74)

/**
 * Sets up the necessary characteristics of a Trie node
 */
class TrieNode(
    var letter: String,
    var progress: String,
    var finishedWord: Boolean,
    var parent: TrieNode?,
    var childLetters: MutableList<Char>,
    var children: MutableList<TrieNode>,
    var weight: Long = 0)

/**
 *
 */
class Trie (english: MutableList<Pair<String, Long>>, trieWords: MutableList<String>) {

    var wordsInTrie = trieWords
    val startRoot = build(english)

    /**
     * Builds a trie from a list of strings representing real words
     * @return the root node of the Trie that holds links to all its children and the rest of the nodes
     */
    private fun build(words: MutableList<Pair<String, Long>>): TrieNode {
        val root = TrieNode(
            "",
            "",
            false,
            null,
            mutableListOf(),
            mutableListOf())
        var holder: TrieNode


        for (wordPair in words) {
            val word = wordPair.first
            holder = root
            var segment = ""

            for (str in word) {
                if (holder.childLetters.contains(str) && segment == holder.progress) {
                    segment += str
                    val index = holder.childLetters.indexOf(str)
                    holder = holder.children[index]

                } else {

                    val node = TrieNode(
                            str.toString(),
                            (holder.progress) + str,
                            false,
                            holder,
                            mutableListOf(),
                            mutableListOf(),
                            alphabetWeights[str.toString()]!!
                        )

                    holder.children.add(node)
                    holder.childLetters.add(str)
                    holder.weight += node.weight
                    holder = node

                    }
                }

            holder.finishedWord = true
            holder.weight = wordPair.second

            }

        return root

        }

    /**
     *
     */
    fun insert(word: String, frequency: Long) {
        if (search(word)) {
            println("Your word is already in the trie")
            return //
        }

        var holder = startRoot
        var segment = ""
        for (str in word) {

            if (segment == holder.progress && holder.childLetters.contains(str) ) {
                segment += str
                val index = holder.childLetters.indexOf(str)
                holder = holder.children[index]



            } else {
                val node = TrieNode(
                    str.toString(),
                    (holder.progress) + str,
                    false,
                    holder,
                    mutableListOf(),
                    mutableListOf(),
                    0
                )

                holder.children.add(node)
                holder.childLetters.add(str)
                holder = node

            }

        }

        holder.finishedWord = true
        holder.weight = frequency
        wordsInTrie.add(word)
    }

    fun pop(word: String): String? {
        var closest: TrieNode = locate(word) ?: return null
        closest.finishedWord = false
        var index = word.length - 1

        while (!closest.finishedWord) {
            val parent = closest.parent

            if (parent != null) {

                parent.children.remove(closest)
                parent.childLetters.remove(word[index])
                closest = parent
                index -= 1
            }
        }
        return word
    }
    /**
     *
     */
    fun search(word: String): Boolean {
        var index = 0
        var holder = startRoot

        while (holder.childLetters.contains(word[index])) {

            if (holder.finishedWord && holder.progress == word) {
                return true
            }

            val childIndex = holder.childLetters.indexOf(word[index])
            holder = holder.children[childIndex]

            if (index+1 == word.length) {
               break
            } else {
                index += 1
            }

        }

        return holder.finishedWord && holder.progress == word
    }

    /**
     *
     */
    fun locate(word: String): TrieNode? {

        var index = 0
        var holder = startRoot

        while (holder.childLetters.contains(word[index])) {

            if (holder.progress == word) {
                return holder
            }

            val childIndex = holder.childLetters.indexOf(word[index])
            holder = holder.children[childIndex]

            if (index+1 == word.length) {
                break
            } else {
                index += 1
            }

        }

        if (holder.progress == word) {
            return holder
        }

        return null
    }

    /**
     *
     */
    fun makeMap(): MutableMap<String, TrieNode> {

        val wordToNodes = mutableMapOf<String, TrieNode>()

        for (w in wordsInTrie) {
            when (val temp = locate(w)) {
                // Checks whether word is in true
                null -> continue

                // Default statement
                else -> wordToNodes[w] = temp
            }
        }

        return wordToNodes
    }
}

fun mostLikelyNext (trieNode: TrieNode): TrieNode {

    var bestPath = trieNode
    var greatestWeight: Long = 0

    for (child in trieNode.children) {
        if (child.weight > greatestWeight) {
            greatestWeight = child.weight
            bestPath = child
        }
    }

    return bestPath
}

val mostCommonLetters = listOf("e", "a", "r", "i", "o", "t", "n", "s", "l", "c", "d")

fun predictText (segment: String, trie: Trie): Triple<String, String, String> {

    val segmentNode = trie.locate(segment)
    val possibilities = mutableListOf<TrieNode>()

    for (child in segmentNode?.children!!) {

        var currNode = child

        while (!currNode.finishedWord) {
            println("stuck")
            currNode = mostLikelyNext(currNode)
        }

        possibilities.add(currNode)
    }

    while (possibilities.size > 3) {
        val poss = possibilities.last()

        if (possibilities[0].weight < possibilities[1].weight) {
            possibilities.removeAt(0)
        }

        if (possibilities[0].weight >= possibilities[1].weight) {
            possibilities.removeAt(1)
        }

        if (poss.progress[segment.length].toString() !in mostCommonLetters) {
            possibilities.removeAt(possibilities.size-1)
        }
    }

    return Triple(possibilities[0].progress, possibilities[1].progress, possibilities[2].progress)
}

fun spellCheck (word: String, errorPos: Int, trie: Trie): String {

    if (errorPos == word.length-1) {
        val seg = word.dropLast(1)
        val closest = trie.locate(seg) ?: return word
        return mostLikelyNext(closest).progress
    }

    var node = trie.startRoot
    var checking = ""
    var corrIndex = errorPos

            for (i in 1 .. word.length) {
        var highestProb: Long = 0
        var best = word[i-1].toString()

        if (i-1 == errorPos) {

            for (wrongPos in node.children ) {

                if (wrongPos.childLetters.contains(word[i])) {

                    val nextIndex = wrongPos.childLetters.indexOf(word[i])

                    if (wrongPos.children[nextIndex].weight>highestProb) {
                        highestProb= wrongPos.children[nextIndex].weight
                        best = wrongPos.letter

                        corrIndex = node.children.indexOf(wrongPos)
                    }
                }
            }

            checking += best
            node = node.children[corrIndex]

        } else {
            val childIndex = node.childLetters.indexOf(word[i-1])
            node = node.children[childIndex]
            checking = node.progress
        }
    }

    return checking
}