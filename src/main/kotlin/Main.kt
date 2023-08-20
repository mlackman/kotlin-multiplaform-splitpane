import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    var leftPaneWidth by remember { mutableStateOf(70.dp) }

    Row {
        Column(modifier = Modifier
            .width(leftPaneWidth)
            .fillMaxHeight()
            .background(Color.Red)
        ) {
            Text("Right pane")
        }
        Column(
            modifier = Modifier
                .width(10.dp)
                .fillMaxHeight()
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                         leftPaneWidth += dragAmount.x.toDp();
                    }
                }
        ) {}
        Column(modifier = Modifier.fillMaxSize().background(Color.Green)) {
            Text("Left pane")
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}