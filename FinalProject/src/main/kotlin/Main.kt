package org.example


import java.io.File
import kotlin.text.iterator

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val wordFrequency = File("unigram_freq.csv")
    val frequencyDict = mutableListOf<Pair<String, Int>>()
    wordFrequency.forEachLine { line ->
        val column = line.split(",")
        frequencyDict.add(Pair(column[0], column[1].toInt()))
    }

    val dict = File("words_alpha.txt")
    val correctDictionary = mutableSetOf<String>()
    dict.forEachLine { line ->
        correctDictionary.add(line)
    }

    val english = mutableListOf<String>()
    for (d in frequencyDict) {
        english.add(d.first)
        if (d.first !in correctDictionary) {
            frequencyDict.remove(d)
        }
    }

    val englishTrie = Trie(frequencyDict, english)
    val englishTrieRoot = englishTrie.startRoot
    println(englishTrieRoot.childLetters)
}

/**
 * Sets up the necessary characteristics of a Trie node
 */
class TrieNode(var letter: String,
               var progress: String,
               var end: Boolean,
               var parent: TrieNode?,
               var childLetters: MutableList<Char>,
               var children: MutableList<TrieNode>,
               var weight: Int = 0)

/**
 *
 */
class Trie (english: MutableList<Pair<String, Int>>, trieWords: MutableList<String>) {

    var wordsInTrie = trieWords
    val startRoot = build(english)

    /**
     * Builds a trie from a list of strings representing real words
     * @return the root node of the Trie that holds links to all its children and the rest of the nodes
     */
    private fun build(words: MutableList<Pair<String, Int>>): TrieNode {
        val root = TrieNode(
             "",
            "",
            false,
            null,
            mutableListOf(),
            mutableListOf())
        var holder: TrieNode
        var segment = ""
        var count = 0

        for (wordPair in words) {
            val word = wordPair.first
            val nodeWeights = wordPair.second/word.length
            count += 1

            holder = root

            var continueCheck = 0
            for (str in word) {
                if (holder.childLetters.contains(word[continueCheck]) && segment == holder.progress) {
                    segment += str
                    val index = holder.childLetters.indexOf(str)
                    holder = holder.children[index]
                    continueCheck += 1

                } else {
                    val node = TrieNode(
                            str.toString(),
                            (holder.progress) + str,
                            false,
                            holder,
                            mutableListOf(),
                            mutableListOf(),
                            nodeWeights
                        )

                    holder.children.add(node)
                    holder.childLetters.add(str)
                    holder = node

                    }
                }

            holder.end = true
            holder.weight = wordPair.second

            }

        return root

        }

    /**
     *
     */
    fun insert(word: String, frequency: Int) {
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
                    mutableListOf()
                )

                holder.children.add(node)
                holder.childLetters.add(str)
                holder = node

            }

        }

        holder.end = true
        holder.weight = frequency
        wordsInTrie.add(word)
    }

    /**
     *
     */
    fun search(word: String): Boolean {

        var index = 0
        var holder = startRoot

        while (holder.childLetters.contains(word[index])) {

            if (holder.end && holder.progress == word) {
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

        return holder.end && holder.progress == word
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
    var greatestWeight = 0

    for (child in trieNode.children) {
        if (child.weight > greatestWeight) {
            greatestWeight = child.weight
            bestPath = child
        }
    }

    return bestPath
}


fun predictText (segment: String, trie: Trie): Pair<String, String> {

    val segmentNode = trie.locate(segment)
    var possibilities = mutableListOf<TrieNode>()

    for (child in segmentNode?.children!!) {
        var currNode = child

        while (!currNode.end) {
            currNode = mostLikelyNext(currNode)
        }

        possibilities.add(currNode)
    }

    while (possibilities.size > 2) {
        if (possibilities[0].weight > possibilities[1].weight) {
            possibilities.removeAt(1)
        }

        if (possibilities[0].weight <= possibilities[1].weight) {
            possibilities.removeAt(0)
        }
    }

    return Pair(possibilities[0].progress, possibilities[1].progress)
}


fun spellCheck (text: String, trie: Trie): String {
    val textBreakup = text.split(" ")
    var fixedText = ""

    for (phrase in textBreakup) {
        if (trie.search(phrase)) {
            fixedText = "$fixedText$phrase "
        } else {

        }
    }

    return fixedText
}