import org.w3c.dom.*
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


private val defaultDocumentBuilder by lazy { DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder() }

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@DslMarker
annotation class XmlDsl

/**
 * Class that handles the building of a [Document] with DSL.
 * For building a document, use [documentBuilder]. For building just a [Node], or more specifically an
 * [Element], use [nodeBuilder]. See [main] in Example.kt for a sample.
 */
@XmlDsl
class DocumentBuilder(
    private val document: Document = defaultDocumentBuilder.newDocument()
){

    private inner class NodeBuilder(nodeName: String, action: Node.() -> Unit) {
        private val node: Node = document.createElement(nodeName).apply(action)
        fun build(): Node = node
    }

    private inner class ElementBuilder(nodeName: String, action: Element.() -> Unit) {
        private val element: Element = document.createElement(nodeName).apply(action)
        fun build(): Element = element
    }

    private inner class TextNodeBuilder(data: String, action: Text.() -> Unit){
        private val textNode: Text = document.createTextNode(data).apply(action)
        fun build(): Text = textNode
    }

    private inner class CommentNodeBuilder(comment: String, action: Comment.() -> Unit){
        private val commentNode: Comment = document.createComment(comment).apply(action)
        fun build(): Comment = commentNode
    }

    private inner class CdataNodeBuilder(data: String, action: CDATASection.() -> Unit){
        private val cDataSection: CDATASection = document.createCDATASection(data).apply(action)
        fun build(): CDATASection = cDataSection
    }

    private inner class AttrNodeBuilder(name: String, action: Attr.() -> Unit){
        private val attrNode: Attr = document.createAttribute(name).apply(action)
        fun build(): Attr = attrNode
    }


    @XmlDsl
    inner class NodeArrayList: ArrayList<Node>(){

        @XmlDsl
        fun element(tagName: String, action: @XmlDsl Element.() -> Unit){
            synchronized(this){
                add(DocumentBuilder(this@DocumentBuilder.document).ElementBuilder(tagName, action).build())
            }
        }

        @XmlDsl
        fun node(nodeName: String, action: @XmlDsl Node.() -> Unit){
            synchronized(this){
                add(DocumentBuilder(this@DocumentBuilder.document).NodeBuilder(nodeName, action).build())
            }
        }

        @XmlDsl
        fun textNode(data: String, action: @XmlDsl Text.() -> Unit){
            synchronized(this){
                add(DocumentBuilder(this@DocumentBuilder.document).TextNodeBuilder(data, action).build())
            }
        }

        @XmlDsl
        fun commentNode(comment: String, action: @XmlDsl Comment.() -> Unit){
            synchronized(this){
                add(DocumentBuilder(this@DocumentBuilder.document).CommentNodeBuilder(comment, action).build())
            }
        }

        @XmlDsl
        fun cDataSection(data: String, action: @XmlDsl CDATASection.() -> Unit){
            synchronized(this){
                add(DocumentBuilder(this@DocumentBuilder.document).CdataNodeBuilder(data, action).build())
            }
        }

        @XmlDsl
        fun attributeNode(name: String, action: @XmlDsl Attr.() -> Unit){
            synchronized(this){
                add(DocumentBuilder(this@DocumentBuilder.document).AttrNodeBuilder(name, action).build())
            }
        }
    }

    @XmlDsl
    inner class NodeBuilderViewModel(private var rootNode: Node? = null) {

        private val nodes = mutableListOf<Node>()

        internal fun nodes(nodeArrayList: NodeArrayList) {
            nodes.addAll(nodeArrayList)
        }

        @XmlDsl
        @Suppress("UNCHECKED_CAST")
        fun rootElement(nodeName: String, action: @XmlDsl Element.() -> Unit) = rootNode(nodeName, action as Node.() -> Unit)

        @XmlDsl
        @OptIn(ExperimentalContracts::class)
        fun rootNode(nodeName: String, action: @XmlDsl Node.() -> Unit){
            contract {
                callsInPlace(action, InvocationKind.AT_MOST_ONCE)
            }
            if(rootNode != null){
                throw Exception("There can only be one root node!")
            }
            rootNode = this@DocumentBuilder.document.createElement(nodeName).apply(action)
            this@DocumentBuilder.document.appendChild(rootNode)
        }

        private fun appendAll(){
            synchronized(this){
                if(rootNode != null){
                    nodes.forEach {
                        if (it is Attr && rootNode is Element){
                            (rootNode as Element).setAttributeNode(it)
                        } else rootNode!!.appendChild(it)

                    }
                }
            }
        }

        internal fun build(): ArrayList<Node> {
            appendAll()
            return ArrayList(nodes)
        }

        internal fun buildDocument(): Document {
            appendAll()
            return this@DocumentBuilder.document
        }

    }

}

@XmlDsl
fun documentBuilder(nodes: @XmlDsl DocumentBuilder.NodeBuilderViewModel.() -> Unit): Document {
    return DocumentBuilder().NodeBuilderViewModel().apply(nodes).buildDocument()
}

@XmlDsl
fun nodeBuilder(document: Document, rootNodeTagName: String, action: Node.() -> Unit): Node{
    return document.createElement(rootNodeTagName).apply(action)
}

fun Node.children(children: @XmlDsl DocumentBuilder.NodeArrayList.() -> Unit){
    val builder = DocumentBuilder(this.ownerDocument).NodeBuilderViewModel(this)
    val result = DocumentBuilder(this.ownerDocument).NodeArrayList().apply(children)
    builder.nodes(result)
    builder.build()
}

fun getNewDocument(): Document = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().newDocument()


@Throws(IOException::class, TransformerException::class)
fun printDocument(doc: Document?) {
    val tf = TransformerFactory.newInstance()
    val transformer: Transformer = tf.newTransformer()
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
    transformer.setOutputProperty(OutputKeys.METHOD, "xml")
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
    transformer.transform(
        DOMSource(doc),
        StreamResult(OutputStreamWriter(System.out, "UTF-8"))
    )
}
