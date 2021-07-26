import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.File


fun String.startsWithUppercase(): Boolean {
    return "[A-Z].*".toRegex().matches(this)
}

fun walkFile(sourceFile: File){
    sourceFile.walk().forEach {
        if(it.extension == "doc"){
            println(it.absolutePath)
        }
    }
}

fun isPalidinome(string: String): Boolean {
    var endIndex = string.length -1
    string.forEachIndexed{ _, char ->

        if(char != string[endIndex]){
            return false
        }
        endIndex--
    }
    return true
}

fun Array<Number>.calculateSum(): Number {
    return this.sumByDouble { it.toDouble() }
}

data class Play(
    val x: String
)

data class Elemetent(val tagName: String, val childtags: List<Elemetent>? = null, val modules: List<String>)

fun main(){
//    walkFile(File("C:/Users/cmmcm/Documents"))
//    println(isPalidinome("racecr"))
//    println("dabs".startsWithUppercase())
    val element1 = Elemetent("root", childtags = listOf(Elemetent("hello", null, emptyList())), emptyList())

    val mapper = ObjectMapper()
    val jsonNode = mapper.createObjectNode()
    jsonNode.put("hello", "world")
    jsonNode.set<ObjectNode>("hello", jsonNode.objectNode().put("foo", "bar"))
    val node = jsonNode.at("/hello/foo")
    println(node.toPrettyString())
    println()
    println(jsonNode.toPrettyString())
    jsonNode.put("test", mapper.writeValueAsString(element1))
    println(jsonNode.toPrettyString())
}