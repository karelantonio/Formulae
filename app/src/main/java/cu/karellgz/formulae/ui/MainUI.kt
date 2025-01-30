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
import cu.karellgz.formulae.ui.main.AboutUI
import cu.karellgz.formulae.ui.main.EditorUI
import cu.karellgz.formulae.ui.main.MoreOptionsUI
import cu.karellgz.formulae.ui.main.PreviewUI
import cu.karellgz.formulae.ui.main.ThemePicker
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
