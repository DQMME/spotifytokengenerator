import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    val root = document.getElementById("root")
    if (root != null) createRoot(root).render(app.create())
}