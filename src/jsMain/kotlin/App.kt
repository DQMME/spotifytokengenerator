import kotlinx.coroutines.MainScope
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1

private val scope = MainScope()

val app = FC<Props> {
    h1 {
        +"This is a Full Stack Kotlin Template"
    }
}