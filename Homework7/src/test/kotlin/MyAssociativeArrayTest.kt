import org.example.MyAssociativeArray
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MyAssociativeArrayTest {
    var professors = MyAssociativeArray<String, String>()


    @Test
    fun set() {
        professors.set("Paul", "Ruvolo")
        professors.set("Sarah", "Spencer-Adams")
        professors.set("Amon", "Millner")

        assertEquals("Millner", professors["Amon"])
        assertEquals("Ruvolo", professors.get("Paul"))

    }

    @Test
    fun contains() {
        assertEquals(false, professors.contains("Sam"))

        professors.set("Sam", "Michalka")
        assertEquals(true, professors.contains("Sam"))
    }

    @Test
    fun get() {
        professors.set("Ben", "Linder")
        assertEquals("Linder", professors.get("Ben"))

        professors.set("Jean", "Huang")
        assertEquals("Huang", professors["Jean"])
    }

    @Test
    fun remove() {
        professors.set("Chhavi", "Goenka")
        assertEquals(true, professors.contains("Chhavi"))
        assertEquals("Goenka", professors.get("Chhavi"))

        val test = professors.remove("Chhavi")
        assertEquals(true, test)
        assertEquals(false, professors.contains("Chhavi"))

        assertEquals(false, professors.remove("Caitrin"))

    }

    @Test
    fun size() {
        professors = MyAssociativeArray()
        assertEquals(0, professors.size())

        professors.set("Gillian", "Epstein")
        assertEquals(1, professors.size())

        professors.set("Chris", "Lee")
        assertEquals(2, professors.size())

        professors.set("Victoria", "Preston")
        assertEquals(3, professors.size())

        professors.set("Joanna", "Pratt")
        assertEquals(4, professors.size())
    }

    @Test
    fun keyValuePairs() {
        professors = MyAssociativeArray()
        professors.set("Paul", "Ruvolo")
        professors.set("Sarah", "Spencer-Adams")
        professors.set("Amon", "Millner")
        professors.set("Ben", "Linder")
        professors.set("Jean", "Huang")

        val ex = mutableListOf(
            Pair("Paul", "Ruvolo"),
            Pair("Sarah", "Spencer-Adams"),
            Pair("Amon", "Millner"),
            Pair("Ben", "Linder"),
            Pair("Jean", "Huang")
        )

        assertEquals(ex.count(), professors.keyValuePairs().count())
    }

}