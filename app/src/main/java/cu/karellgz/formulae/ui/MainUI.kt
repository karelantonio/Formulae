package cu.karellgz.formulae.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Create
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.R
import cu.karellgz.formulae.latex.LaTeX
import cu.karellgz.formulae.ui.theme.FormulaeTheme
import cu.karellgz.formulae.utils.Theme
import cu.karellgz.formulae.utils.mutableStateThemeSaver
import java.io.File

@Composable
@Preview(name = "Main UI preview")
fun MainUI() {
    FormulaeTheme {

        val ctl = rememberNavController()
        val content = rememberSaveable { mutableStateOf("") }
        val theme = rememberSaveable(
            saver = mutableStateThemeSaver,
            init = { mutableStateOf(Theme.BLACK_ON_WHITE) })

        Scaffold(
            topBar = { AppBar(ctl) },
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {

            NavHost(
                modifier = Modifier.padding(it),
                navController = ctl,
                startDestination = "editor"
            ) {
                composable("editor") {
                    EditorUI(
                        ctl,
                        value = content.value,
                        onChange = { new -> content.value = new })
                }

                dialog("themePicker") {
                    ThemePicker(ctl, theme.value, onSelected = { thm ->
                        theme.value = thm
                    })
                }

                dialog("preview") {
                    PreviewUI(ctl, value = content.value, theme = theme.value)
                }

                dialog("moreOptions") {
                    MoreOptionsUI(ctl)
                }

                dialog("about") {
                    AboutUI(ctl)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(ctl: NavController) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = { ctl.navigate("moreOptions") }) {
                Icon(imageVector = Icons.TwoTone.MoreVert, contentDescription = "More options")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.equation),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp),
                    contentDescription = "Euler raised to the power of I times Pi"
                )

                Text(
                    "Editor",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = {
            ElevatedButton(onClick = { ctl.navigate("preview") }) {
                Text("Preview")
            }
        }
    )
}

@Composable
@Preview(name = "Preview the editor")
fun EditorPreview() {
    FormulaeTheme {
        Surface {
            EditorUI(rememberNavController(), "This should be some string idk") {}
        }
    }
}

@Composable
fun EditorUI(ctl: NavHostController, value: String, onChange: (String) -> Unit) {

    val scroll = rememberScrollState()

    Column {

        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = TextStyle.Default.copy(fontFamily = FontFamily.Monospace),
            value = value,
            placeholder = {
                Text("Your formula goes here", fontFamily = FontFamily.Monospace)
            },
            onValueChange = onChange,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(0.dp)
                .verticalScroll(scroll),
            readOnly = false
        )
    }
}


@Composable
@Preview(name = "Preview the preview lol", showBackground = true)
fun PreviewPreview() {
    FormulaeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PreviewUI(
                ctl = rememberNavController(),
                value = "x^2 - 2abx + ab = (x-a)(x-b)",
                isPreview = true
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PreviewUI(
    ctl: NavHostController,
    value: String,
    theme: Theme = Theme.BLACK_ON_WHITE,
    isPreview: Boolean = false
) {

    val scroll = rememberScrollState()
    val error = rememberSaveable { mutableStateOf<String?>(null) }
    val saveClicked = rememberSaveable { mutableStateOf(false) }
    val ctx = LocalContext.current

    Dialog(
        onDismissRequest = { ctl.popBackStack() },
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp, 24.dp)
                .fillMaxWidth()
                .verticalScroll(scroll)
        ) {

            // The webview
            Surface {
                LaTeX(
                    formula = value,
                    save = saveClicked.value,
                    onError = { error.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    theme = theme,
                    onSaved = {
                        saveClicked.value = false
                        val uri = FileProvider.getUriForFile(
                            ctx,
                            "${ctx.packageName}.fileprovider",
                            File(it)
                        )

                        // The intent
                        val share = Intent(Intent.ACTION_SEND)
                        share.type = "image/png"
                        share.putExtra(Intent.EXTRA_STREAM, uri)
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        ctx.startActivity(Intent.createChooser(share, "Share with..."))
                    }
                )
            }


            if (error.value != null || isPreview) {
                Text(
                    "${error.value}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
            ) {

                TextButton(onClick = { saveClicked.value = true }) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.TwoTone.Share,
                        contentDescription = "Share"
                    )
                    Text("Share")
                }

                Spacer(modifier = Modifier.weight(1f))

                TextButton(onClick = { ctl.navigate("themePicker") }) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.TwoTone.Create,
                        contentDescription = "Theme"
                    )
                    Text("Theme")
                }
            }
        }
    }
}

@Preview
@Composable
fun ThemePickerPreview() {
    FormulaeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ThemePicker(rememberNavController(), Theme.BLACK_ON_WHITE) {}
        }
    }
}

@Composable
fun ThemePicker(ctl: NavHostController, selected: Theme?, onSelected: (Theme) -> Unit) {
    Dialog(onDismissRequest = { ctl.popBackStack() }) {
        Card {
            Column(modifier = Modifier.padding(24.dp, 12.dp)) {

                Text(
                    text = "Select the theme:",
                    modifier = Modifier.padding(0.dp, 6.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                val themes: List<Pair<String, Theme>> = listOf(
                    Pair("White on black background", Theme.WHITE_ON_BLACK),
                    Pair("Black on white background", Theme.BLACK_ON_WHITE),
                )

                for (pr in themes) {
                    val name = pr.first
                    val theme = pr.second

                    TextButton(onClick = { onSelected(theme); ctl.popBackStack() }) {
                        Text(
                            " x ",
                            color = Color(android.graphics.Color.parseColor(theme.foreground)),
                            modifier = Modifier
                                .padding(6.dp)
                                .background(Color(android.graphics.Color.parseColor(theme.background)))
                        )
                        Text(
                            name,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsUI(ctl: NavController) {
    AlertDialog(onDismissRequest = { ctl.popBackStack() }) {}
}

@Composable
fun AboutUI(ctl: NavController) {

}