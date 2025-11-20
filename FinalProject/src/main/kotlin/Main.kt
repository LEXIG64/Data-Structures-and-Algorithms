package org.example

import kotlin.text.iterator

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val text = Trie(listOf("apple", "bears", "crown"))
    println(text.search("apple") )
    text.insert("deer")
    println(text.search("deer"))
    text.insert("deep")
    println(text.search("deep"))
    println(text.startRoot.children.last().childletters)
}

class TrieNode(var letter: String,
               var progress: String,
               var end: Boolean,
               var parent: TrieNode?,
               var childletters: MutableList<Char>,
               var children: MutableList<TrieNode>) {}


class Trie (english: List<String>) {

    var trieSize = english.size
    val startRoot = build(english)

    private fun build(words: List<String>): TrieNode {
        val root = TrieNode(
             "",
            "",
            false,
            null,
            mutableListOf(),
            mutableListOf())
        var holder: TrieNode
        var segment = ""

        for (word in words) {

            holder = root

            var continueCheck = 0
            for (str in word) {
                if (holder.childletters.contains(word[continueCheck]) && segment == holder.progress) {
                    segment += str
                    val index = holder.childletters.indexOf(str)
                    holder = holder.children[index]
                    continueCheck += 1

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
                        holder.childletters.add(str)
                        holder = node

                    }
                }

            holder.end = true

            }

        return root

        }

    fun insert(word: String) {
        var holder = startRoot
        var segment = ""
        for (str in word) {

            if (segment == holder.progress && holder.childletters.contains(str) ) {
                segment += str
                val index = holder.childletters.indexOf(str)
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
                holder.childletters.add(str)
                holder = node

            }

        }

        holder.end = true
        trieSize += 1
    }

    fun search(word: String): Boolean {

        var index = 0
        var holder = startRoot

        while (holder.childletters.contains(word[index])) {

            if (holder.end && holder.progress == word) {
                return true
            }

            val childIndex = holder.childletters.indexOf(word[index])
            holder = holder.children[childIndex]

            if (index+1 == word.length) {
               break
            } else {
                index += 1
            }

        }

        return holder.end && holder.progress == word
    }

    fun makeMap(): MutableMap<String, TrieNode> {

        val wordToNodes = mutableMapOf<String, TrieNode>()

        while (wordToNodes.count() != trieSize) {


        }

        return wordToNodes
    }
}





