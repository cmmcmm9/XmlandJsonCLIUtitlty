

fun main(){
    val document = documentBuilder {
        rootElement("mustafar"){

            children {
                attributeNode("hot"){
                    value = "true"
                }
                attributeNode("id"){
                    value = "root"
                }
                element("hill"){

                    children {
                        element("obi-wan"){
                            setAttribute("highGround", "true")

                            children {
                                textNode("It's no use Anakin, I have the high ground."){}
                            }
                        }

                        element("anakin"){
                            textContent = "I HATE YOU!!!!!!!!!!!!!!!!"
                            children {
                                commentNode("**Queue burning limbs**"){}
                            }
                        }
                    }
                }
            }
            setIdAttribute("id", true)
        }
    }
    printDocument(document)

    val characterReflectionNode = nodeBuilder(document, "characterReflectionNode"){
        children {
            cDataSection("Obi Wan looks down at his fallen brother, wondering where he went wrong."){

            }
        }
    }

    val rootMustafarNode = document.getElementById("root")
    rootMustafarNode.appendChild(characterReflectionNode)
    println("===================================================================")
    printDocument(document)
}