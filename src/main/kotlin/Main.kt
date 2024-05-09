import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MyState {
    val value = mutableStateOf("mika")
    val folders = mutableStateListOf(
        Folder("Folder 1", listOf("item 1", "item 2")),
        Folder("Folder 2", listOf("item 1", "item 2"))
    )

    fun addFolder(name: String) {
        folders.add(Folder(name, emptyList()))
    }
}


class Folder(name: String, contents: List<String>) {
    val name = mutableStateOf(name)
    val contents = mutableStateOf(contents.toMutableList())

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App(state: MyState) {
    // val (value, changeValue) = state()
    val value = remember { state.value }
    val folders = remember { state.folders }
    Column {
        Text(value.value)
        // Text(text = "click me", modifier = Modifier.onClick { value.value = "new value"})
        Text(text = "click me", modifier = Modifier.onClick { state.addFolder("new folder")})

        VerticalSplitPanel(
            leftPanel = {
                Row(modifier = Modifier.fillMaxSize()) {
                    Column {
                        folders.forEach {
                            Folder(
                                title = { Text(it.name.value) },
                                contents = it.contents.value.map { { Text(it) } }
                            )
                        }
                    }
                }
            },
            rightPanel = {
                HorizontalSplitPanel(
                    topPanel = {
                       Row {
                           Text("Top panel")
                       }
                    },
                    bottomPanel = {
                        Text("bottom panel")

                    }

                )
            }

        )
    }
}

@Composable
fun VerticalSplitPanel(
    leftPanel: @Composable () -> Unit,
    rightPanel: @Composable () -> Unit,
) {
    var leftPaneWidth by remember { mutableStateOf(150.dp) }
    Row {
        Column(
            modifier = Modifier
                .width(leftPaneWidth)
                .fillMaxHeight()
                .background(Color.Red)
        ) {
            leftPanel()
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
            rightPanel()
        }
    }
}

@Composable
fun HorizontalSplitPanel(
    topPanel: @Composable () -> Unit,
    bottomPanel: @Composable () -> Unit,
) {
    var topPanelHeight by remember { mutableStateOf(150.dp) }
    Column {
        // Top panel
        Row (
            modifier = Modifier
                .height(topPanelHeight)
                .fillMaxWidth()
                .background(Color.Cyan)
        ) {
            topPanel()
        }

        // Bottom panel
        Row (
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        topPanelHeight += dragAmount.y.toDp();
                    }
                }
        ) {}
        Row(modifier = Modifier.fillMaxSize()) {
            bottomPanel()
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Folder(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    contents: List<@Composable () -> Unit>
) {
    var folderOpen by remember { mutableStateOf(false )  }

    Column {
        Column(modifier=Modifier.onClick { folderOpen = !folderOpen }) {
            title()
        }
        if (folderOpen) {
            contents.forEach {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    it()
                }
            }

        }
    }
}


fun main() {
    val state = MyState()
    runBlocking {
        launch {
            delay(4000L)
            state.addFolder("New delayed folder")

        }
        application {
            Window(onCloseRequest = ::exitApplication) {
                App(state)
            }
        }
    }

}

